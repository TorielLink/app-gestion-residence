package iut.dam.gestionresidence.ui.fragments.list_habitats;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.databinding.FragmentReservationBinding;
import iut.dam.gestionresidence.entities.Appliance;
import iut.dam.gestionresidence.entities.TokenManager;

public class ReservationFragment extends Fragment {
    private final List<Appliance> appliances = new ArrayList<>();
    private static final double DAILY_ENERGY_THRESHOLD = 1000; // TODO : get max wattage form DB

    public ReservationFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentReservationBinding binding = FragmentReservationBinding.inflate(inflater, container,
                false);
        View root = binding.getRoot();

        Spinner spinnerAppliances = binding.spinnerAppliances;
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getAppliances.php?token="
                + TokenManager.getToken();

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            try {
                JSONArray jsonArray = new JSONArray(result);
                List<String> appliancesNames = new ArrayList<>();

                appliancesNames.add("None");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    appliancesNames.add(name);
                }

                ArrayAdapter<String> appliancesAdapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, appliancesNames);

                appliancesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAppliances.setAdapter(appliancesAdapter);

                spinnerAppliances.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedOption = appliancesAdapter.getItem(position);
                        assert selectedOption != null;
                        if (!selectedOption.equals("None")) {
                            Toast.makeText(requireContext(), "Selected Appliance: " + selectedOption,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            } catch (JSONException ex) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.menu_reservation))
                        .setMessage(getString(R.string.error_get_appliances))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        Spinner spinnerTimeSlot = binding.spinnerTimeSlot;

        String[] timeSlotOptions = {"0h-1h", "1h-2h", "2h-3h", "3h-4h", "4h-5h", "5h-6h", "6h-7h"
                , "7h-8h", "8h-9h", "9h-10h", "10h-11h", "11h-12h", "12h-13h", "13h-14h", "14h-15h"
                , "15h-16h", "16h-17h", "17h-18h", "18h-19h", "19h-10h", "20h-21h", "21h-22h"
                , "22h-23h", "23h-0h"};
        ArrayAdapter<?> timeSlotAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, timeSlotOptions);

        spinnerTimeSlot.setAdapter(timeSlotAdapter);
        spinnerTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) timeSlotAdapter.getItem(position);
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
        double totalConsumption = calculateTotalConsumption(); // TODO : rajouter la consommation du créneau

        if (totalConsumption > DAILY_ENERGY_THRESHOLD) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(getString(R.string.txt_confirm_reservation));
            builder.setMessage(getString(R.string.txt_consumption_exceeds));
            builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                actuateCoins(-5);
                storeReservationInDatabase();
                Toast.makeText(requireContext(), getString(R.string.txt_reservation_is_confirm),
                        Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            });
            builder.setNegativeButton(getString(R.string.no), (dialog, which) -> {
                actuateCoins(10);
                appliances.clear();
                Toast.makeText(requireContext(), getString(R.string.txt_reservation_is_cancelled),
                        Toast.LENGTH_SHORT).show();
            });
            builder.show();
        } else {
            storeReservationInDatabase();
            Toast.makeText(requireContext(), getString(R.string.txt_reservation_is_confirm), Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        }
    }

    private double calculateTotalConsumption() {
        double totalConsumption = 3000.0;
        for (Appliance appliance : appliances) {
            totalConsumption += appliance.getWattage();
        }
        return totalConsumption;
    }

    private void actuateCoins(int coins) {
        String urlString =
                "http://remi-lem.alwaysdata.net/gestionResidence/changeCoins.php?token="
                        + TokenManager.getToken() + "&coins=" + coins;

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            if(result == null)
                Log.d(TAG, "No response from the server!!!");
            else {
                if(result.equals("OK"))
                    Toast.makeText(requireContext(), getString(R.string.success_change_coins),
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(requireContext(), getString(R.string.error_change_coins),
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeReservationInDatabase() {
        //TODO: faire le stockage dans la base
    }
}