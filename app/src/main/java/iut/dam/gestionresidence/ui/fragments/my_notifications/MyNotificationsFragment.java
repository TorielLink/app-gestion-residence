package iut.dam.gestionresidence.ui.fragments.my_notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import iut.dam.gestionresidence.adapter.NotificationAdapter;
import iut.dam.gestionresidence.databinding.FragmentListHabitatsBinding;
import iut.dam.gestionresidence.databinding.FragmentMyNotificationsBinding;
import iut.dam.gestionresidence.entities.Notification;

public class MyNotificationsFragment extends Fragment {

    private FragmentMyNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = binding.listNotifications;

        List<Notification> itemList = new ArrayList<>();
        //TODO
        itemList.add(new Notification("Alerte de dépassement", "Dépassement de wattage pour le créneau sélectionné"));
        itemList.add(new Notification("Libération d'un créneau", "Un voisin a libéré un des créneaux que vous désiriez"));

        NotificationAdapter adapter = new NotificationAdapter(requireContext(), itemList);

        listView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}