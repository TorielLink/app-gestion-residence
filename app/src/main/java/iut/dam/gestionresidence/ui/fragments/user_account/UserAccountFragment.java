package iut.dam.gestionresidence.ui.fragments.user_account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import iut.dam.gestionresidence.databinding.FragmentUserAccountBinding;

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

        //TODO : Ne fonctionne pas
        Bundle bundle = getArguments();
        if (bundle != null) {
            String firstName = bundle.getString("firstName");
            String lastName = bundle.getString("lastName");
            String email = bundle.getString("email");
            String pwd = bundle.getString("password");

            binding.editTextFirstName.setText(firstName);
            binding.editTextLastName.setText(lastName);
            binding.editTextEmail.setText(email);
            binding.editTextPassword.setText(pwd);
        }

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