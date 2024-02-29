package iut.dam.gestionresidence.ui.fragments.my_preferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import iut.dam.gestionresidence.databinding.FragmentMyPreferencesBinding;

public class MyPreferencesFragment extends Fragment {

    private FragmentMyPreferencesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyPreferencesViewModel slideshowViewModel =
                new ViewModelProvider(this).get(MyPreferencesViewModel.class);

        binding = FragmentMyPreferencesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMyPreferences;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}