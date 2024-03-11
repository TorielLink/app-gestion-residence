package iut.dam.gestionresidence.ui.fragments.user_account;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;


import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.databinding.FragmentUserAccountBinding;
import iut.dam.gestionresidence.entities.TokenManager;

public class UserAccountFragment extends Fragment {
    private FragmentUserAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getUserName.php?token="
                + TokenManager.getToken();

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            JSONObject jsonObject;
            Log.d("debugRemi", urlString);
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

        CheckBox checkBoxFirstName = binding.checkboxFirstName;
        CheckBox checkBoxLastName = binding.checkboxLastName;
        CheckBox checkboxPassword = binding.checkboxPassword;

        EditText editTextFirstName = binding.editTextFirstName;
        EditText editTextLastName = binding.editTextLastName;
        EditText editTextPassword = binding.editTextPassword;

        binding.btnConfirmChanges.setOnClickListener(v -> {
            //TODO: récupérer les anciennes valeurs (ou gérer depuis serveur les chaînes vides)
            String firstName = String.valueOf(editTextFirstName.getText());
            String lastName = String.valueOf(editTextLastName.getText());
            String password = String.valueOf(editTextPassword.getText());

            if (checkBoxFirstName.isChecked())
                firstName = String.valueOf(binding.editTextFirstName.getText());
            if (checkBoxLastName.isChecked())
                lastName = String.valueOf(binding.editTextLastName.getText());
            if (checkboxPassword.isChecked())
                password = String.valueOf(binding.editTextPassword.getText());

            String urlStringUserModif =
                    "http://remi-lem.alwaysdata.net/gestionResidence/changeUserParameters.php?token="
                    + TokenManager.getToken() + "&firstname=" + firstName + "&lastname=" + lastName
                    + "&password=" + password;

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
        });

        checkBoxFirstName.setOnCheckedChangeListener((buttonView, isChecked) ->
                editTextFirstName.setEnabled(isChecked));
        checkBoxLastName.setOnCheckedChangeListener((buttonView, isChecked) ->
                editTextLastName.setEnabled(isChecked));
        checkboxPassword.setOnCheckedChangeListener((buttonView, isChecked) ->
                editTextPassword.setEnabled(isChecked));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}