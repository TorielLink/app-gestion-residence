package iut.dam.gestionresidence.ui.fragments.list_habitats;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import iut.dam.gestionresidence.databinding.FragmentReservationBinding;
import iut.dam.gestionresidence.entities.Appliance;

public class ReservationFragment extends Fragment {
    private final List<Appliance> appliances = new ArrayList<>();
    private static final double DAILY_ENERGY_THRESHOLD = 1000;

    public ReservationFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentReservationBinding binding = FragmentReservationBinding.inflate(inflater, container,
                false);
        View root = binding.getRoot();

        Spinner spinnerAppliances = binding.spinnerAppliances;

        String[] appliancesOptions = {"Option 1", "Option 2", "Option 3"}; // TODO: get user's appliances
        ArrayAdapter<?> appliancesAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, appliancesOptions);

        spinnerAppliances.setAdapter(appliancesAdapter);
        spinnerAppliances.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) appliancesAdapter.getItem(position);
                Toast.makeText(requireContext(), "Item: " + selectedOption,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnReserve = binding.btnReserve;

        btnReserve.setOnClickListener(v -> confirmReservation());

        return root;
    }

    private void confirmReservation() {
        double totalConsumption = calculateTotalConsumption();

        if (totalConsumption > DAILY_ENERGY_THRESHOLD) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirm Reservation");
            builder.setMessage("Total consumption exceeds daily threshold. Do you want to continue?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                //TODO: mettre malus éco-coins
                storeReservationInDatabase();
                Toast.makeText(requireContext(), "Reservation confirmed!",
                        Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                //TODO: mettre bonus éco-coins
                appliances.clear();
                Toast.makeText(requireContext(), "Reservation cancelled.",
                        Toast.LENGTH_SHORT).show();
            });
            builder.show();
        } else {
            storeReservationInDatabase();
            Toast.makeText(requireContext(), "Reservation confirmed!", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateTotalConsumption() { // TODO : rajouter la consommation du créneau
        double totalConsumption = 0.0;
        for (Appliance appliance : appliances) {
            totalConsumption += appliance.getWattage();
        }
        return totalConsumption;
    }

    private void storeReservationInDatabase() {
        //TODO: faire le stockage dans la base
    }
}