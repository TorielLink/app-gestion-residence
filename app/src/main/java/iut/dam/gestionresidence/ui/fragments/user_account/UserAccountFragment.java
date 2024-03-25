package iut.dam.gestionresidence.ui.fragments.user_account;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.databinding.FragmentUserAccountBinding;
import iut.dam.gestionresidence.entities.TokenManager;

public class UserAccountFragment extends Fragment {
    private FragmentUserAccountBinding binding;
    private ActivityResultLauncher<Intent> galleryLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserAccountBinding.inflate(inflater, container, false);

        ImageView imgUser = binding.imgUserProfile;
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getProfilePicture.php?token="+TokenManager.getToken();
        Ion.with(this)
                .load(urlString)
                .asBitmap()
                .setCallback((e, bitmap) -> {
                    if (e == null && bitmap != null) {
                        imgUser.setImageBitmap(bitmap);
                    } else {
                        imgUser.setImageResource(R.drawable.user_logo);
                    }
                });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getUserDataFromServer();

        CheckBox checkBoxFirstName = binding.checkboxFirstName;
        CheckBox checkBoxLastName = binding.checkboxLastName;
        CheckBox checkBoxPassword = binding.checkboxPassword;

        EditText editTextFirstName = binding.editTextFirstName;
        EditText editTextLastName = binding.editTextLastName;
        EditText editTextPassword = binding.editTextPassword;

        binding.btnConfirmChanges.setOnClickListener(v ->
                btnConfirmChangesClicked(editTextFirstName, editTextLastName, editTextPassword,
                        checkBoxFirstName, checkBoxLastName, checkBoxPassword));

        checkBoxFirstName.setOnCheckedChangeListener((buttonView, isChecked) ->
                editTextFirstName.setEnabled(isChecked));
        checkBoxLastName.setOnCheckedChangeListener((buttonView, isChecked) ->
                editTextLastName.setEnabled(isChecked));
        checkBoxPassword.setOnCheckedChangeListener((buttonView, isChecked) ->
                editTextPassword.setEnabled(isChecked));

        ImageView imgUserProfile = binding.imgUserProfile;

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            // Assuming you have a method to convert Uri to byte[]
                            byte[] imageData = convertUriToByteArray(selectedImageUri);
                            if (imageData != null) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media
                                            .getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                                } catch (IOException e) {
                                    Log.e(TAG, "Erreur : " + e.getMessage());
                                }
                                imgUserProfile.setImageBitmap(bitmap);
                                sendImageDataToServer(imageData);
                            }
                        }
                    }
                });

        imgUserProfile.setOnClickListener(v -> openGallery(galleryLauncher));

        setNbEcoCoins(binding.txtEcoCoin);
    }

    private void setNbEcoCoins(TextView textViewEcoCoin) {
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getUserName.php?token="
                + TokenManager.getToken();
        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            int nb_coins;
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(result);
                nb_coins = Integer.parseInt(jsonObject.getString("nb_coins"));
            } catch (JSONException ex) {
                nb_coins = 5;
            }
            textViewEcoCoin.setText(getString(R.string.my_eco_coins_text, nb_coins));
        });

    }

    private void btnConfirmChangesClicked(EditText editTextFirstName, EditText editTextLastName,
                                          EditText editTextPassword, CheckBox checkBoxFirstName,
                                          CheckBox checkBoxLastName, CheckBox checkBoxPassword) {
        String firstName = String.valueOf(editTextFirstName.getText());
        String lastName = String.valueOf(editTextLastName.getText());
        String password = String.valueOf(editTextPassword.getText());

        if (checkBoxFirstName.isChecked())
            firstName = String.valueOf(binding.editTextFirstName.getText());
        if (checkBoxLastName.isChecked())
            lastName = String.valueOf(binding.editTextLastName.getText());
        if (checkBoxPassword.isChecked())
            password = String.valueOf(binding.editTextPassword.getText());

        String urlStringUserModif =
                "http://remi-lem.alwaysdata.net/gestionResidence/changeUserParameters.php?token="
                        + TokenManager.getToken() + "&firstname=" + firstName + "&lastname=" +
                        lastName + "&password=" + password;

        Ion.with(this).load(urlStringUserModif).asString().setCallback((e, result) -> {
            if(result == null)
                Log.d(TAG, "No response from the server!!!");
            else {
                if(result.equals("OK")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.success_change_user_parameters))
                            .setPositiveButton(android.R.string.ok, (dialog, which) ->
                                    dialog.dismiss())
                            .show();
                }
                else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.error_change_user_parameters))
                            .setPositiveButton(android.R.string.ok,
                                    (dialog, which) -> dialog.dismiss())
                            .show();
                }
            }
        });
        checkBoxFirstName.setChecked(false);
        checkBoxLastName.setChecked(false);
        checkBoxPassword.setChecked(false);
    }

    private void getUserDataFromServer() {
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getUserName.php?token="
                + TokenManager.getToken();

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(result);
                String firstName = jsonObject.getString("firstname");
                String lastName = jsonObject.getString("lastname");
                String email = jsonObject.getString("email");
                binding.editTextFirstName.setText(firstName);
                binding.editTextLastName.setText(lastName);
                binding.editTextEmail.setText(email);

            } catch (JSONException ex) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.menu_user_account))
                        .setMessage(getString(R.string.error_get_info_account))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    private void openGallery(ActivityResultLauncher<Intent> galleryLauncher) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void sendImageDataToServer(byte[] imageData) {
        String url = "http://remi-lem.alwaysdata.net/gestionResidence/updateProfilePicture.php?token="
        + TokenManager.getToken();

        Ion.with(requireContext())
                .load("POST", url)
                .setMultipartParameter("photo", Base64.encodeToString(imageData, Base64.DEFAULT))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result.equals("OK")) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(getString(R.string.success_change_user_parameters))
                                    .setPositiveButton(android.R.string.ok, (dialog, which) ->
                                            dialog.dismiss())
                                    .show();
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(getString(R.string.error_change_user_parameters))
                                    .setPositiveButton(android.R.string.ok, (dialog, which) ->
                                            dialog.dismiss())
                                    .show();
                        }
                    }
                });
    }


    // Helper method to convert Uri to byte[]
    private byte[] convertUriToByteArray(Uri uri) {
        try {
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}