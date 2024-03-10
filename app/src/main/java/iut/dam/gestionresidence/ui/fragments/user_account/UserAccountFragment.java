package iut.dam.gestionresidence.ui.fragments.user_account;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.adapter.HabitatAdapter;
import iut.dam.gestionresidence.databinding.FragmentUserAccountBinding;
import iut.dam.gestionresidence.entities.Appliance;
import iut.dam.gestionresidence.entities.Habitat;
import iut.dam.gestionresidence.entities.TokenManager;
import iut.dam.gestionresidence.entities.User;

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
            JSONObject jsonObject = null;
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
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });

        binding.buttonUpdatePassword.setOnClickListener(v -> {
            // TODO: Ajouter màj mdp
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}