package iut.dam.gestionresidence.ui.fragments.list_habitats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import iut.dam.gestionresidence.databinding.FragmentListHabitatsBinding;

public class ListHabitatsFragment extends Fragment {

    private FragmentListHabitatsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListHabitatsViewModel galleryViewModel =
                new ViewModelProvider(this).get(ListHabitatsViewModel.class);

        binding = FragmentListHabitatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textListHabitats;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}