package iut.dam.gestionresidence.ui.fragments.my_notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import iut.dam.gestionresidence.databinding.FragmentMyNotificationsBinding;

public class MyNotificationsFragment extends Fragment {

    private FragmentMyNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyNotificationsViewModel slideshowViewModel =
                new ViewModelProvider(this).get(MyNotificationsViewModel.class);

        binding = FragmentMyNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.txtMyNotifications;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}