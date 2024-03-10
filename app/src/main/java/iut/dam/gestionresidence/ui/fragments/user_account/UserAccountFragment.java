package iut.dam.gestionresidence.ui.fragments.user_account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.databinding.FragmentMyHabitatBinding;

public class UserAccountFragment extends Fragment {
    private FragmentMyHabitatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setTitle("First title");
        return inflater.inflate(R.layout.fragment_user_account, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}