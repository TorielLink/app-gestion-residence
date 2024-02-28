package iut.dam.gestionresidence.ui.liste_habitants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import iut.dam.gestionresidence.databinding.FragmentListeHabitantsBinding;

public class ListeHabitantsFragment extends Fragment {

    private FragmentListeHabitantsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListeHabitantsViewModel galleryViewModel =
                new ViewModelProvider(this).get(ListeHabitantsViewModel.class);

        binding = FragmentListeHabitantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textListeHabitants;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}