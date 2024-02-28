package iut.dam.gestionresidence.ui.mon_habitat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import iut.dam.gestionresidence.databinding.FragmentMonHabitatBinding;


public class MonHabitatFragment extends Fragment {

    private FragmentMonHabitatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MonHabitatViewModel homeViewModel =
                new ViewModelProvider(this).get(MonHabitatViewModel.class);

        binding = FragmentMonHabitatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMonHabitat;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}