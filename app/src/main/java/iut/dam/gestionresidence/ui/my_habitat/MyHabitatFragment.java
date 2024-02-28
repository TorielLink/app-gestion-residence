package iut.dam.gestionresidence.ui.my_habitat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import iut.dam.gestionresidence.databinding.FragmentMyHabitatBinding;


public class MyHabitatFragment extends Fragment {

    private FragmentMyHabitatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyHabitatViewModel homeViewModel =
                new ViewModelProvider(this).get(MyHabitatViewModel.class);

        binding = FragmentMyHabitatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMyHabitat;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}