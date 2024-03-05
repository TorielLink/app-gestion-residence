package iut.dam.gestionresidence.ui.fragments.list_habitats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.adapter.HabitatAdapter;
import iut.dam.gestionresidence.databinding.FragmentListHabitatsBinding;
import iut.dam.gestionresidence.entities.Appliance;
import iut.dam.gestionresidence.entities.Habitat;
import iut.dam.gestionresidence.entities.User;

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

        ListView list = binding.listHabitat;

        //TODO recuperer les habitats sur le serveur WEB via l'API

        ArrayList<Habitat> habitats = new ArrayList<>();
        ArrayList<Appliance> appliances = new ArrayList<>();
        appliances.add(new Appliance(1, "Washing Machine", "WM04", 1600));
        appliances.add(new Appliance(2, "Aspirateur", "Philips X123", 600));
        habitats.add(new Habitat(0, new User("Martin Duchemin"), 2, 4.5, appliances));

        ArrayList<Appliance> appliances2 = new ArrayList<>();
        appliances2.add(new Appliance(4, "Fer à repasser", "WM04", 500));
        appliances2.add(new Appliance(2, "Aspirateur", "Philips X123", 600));
        habitats.add(new Habitat(1, new User("Martine Delaroute"), 4, 6.0, appliances2));

        ArrayList<Appliance> appliances3 = new ArrayList<>();
        appliances3.add(new Appliance(3, "Cooking Machine", "WM04", 450));
        appliances3.add(new Appliance(2, "Aspirateur", "Philips X123", 600));
        habitats.add(new Habitat(2, new User("Jaques Delalé"), 0, 8.0, appliances3));

        HabitatAdapter adapter = new HabitatAdapter(requireContext(), habitats);
        list.setAdapter(adapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}