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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.databinding.FragmentReservationBinding;
import iut.dam.gestionresidence.entities.Appliance;
import iut.dam.gestionresidence.entities.TokenManager;

public class ReservationFragment extends Fragment {
    private final List<Appliance> appliances = new ArrayList<>();
    private int order = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentReservationBinding binding = FragmentReservationBinding.inflate(inflater, container,
                false);
        View root = binding.getRoot();

        Spinner spinnerAppliances = binding.spinnerAppliances;
        populateSpinnerAppliances(spinnerAppliances);

        Spinner spinnerTimeSlot = binding.spinnerTimeSlot;
        populateSpinnerTimeSlot(spinnerTimeSlot);

        DatePicker datePicker = binding.datePickerSelectDate;

        Button btnReserve = binding.btnReserve;
        btnReserve.setOnClickListener(v -> confirmReservation(datePicker, spinnerTimeSlot));

        return root;
    }

    private void populateSpinnerTimeSlot(Spinner spinnerTimeSlot) {
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
                Toast.makeText(requireContext(), selectedOption,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void populateSpinnerAppliances(Spinner spinnerAppliances) {
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getAppliances.php?token="
                + TokenManager.getToken();

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            try {
                JSONArray jsonArray = new JSONArray(result);
                List<Appliance> appliancesList = new ArrayList<>();

                appliancesList.add(new Appliance(-1, "None", "", 0));

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    String ref = jsonObject.getString("reference");
                    int wattage = jsonObject.getInt("wattage");
                    appliancesList.add(new Appliance(id, name, ref, wattage));
                }

                List<String> appliancesListName = new ArrayList<>();
                for (Appliance appliance :
                        appliancesList) {
                    appliancesListName.add(appliance.getName());
                }

                ArrayAdapter<String> appliancesAdapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, appliancesListName);

                appliancesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAppliances.setAdapter(appliancesAdapter);

                spinnerAppliances.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedApplianceName = appliancesAdapter.getItem(position);
                        if (selectedApplianceName != null && !selectedApplianceName.equals("None")) {
                            Appliance selectedAppliance = findApplianceByName(appliancesList, selectedApplianceName);
                            if (selectedAppliance != null) {
                                appliances.add(selectedAppliance);
                            }
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
    }

    private Appliance findApplianceByName(List<Appliance> appliances, String name) {
        for (Appliance appliance : appliances) {
            if (appliance.getName().equals(name)) {
                return appliance;
            }
        }
        return null;
    }

    private void confirmReservation(DatePicker datePicker, Spinner spinnerHours) {
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getTimeSlot.php?token="
                + TokenManager.getToken();
        Ion.with(this).load(urlString).asString().setCallback((e, result) -> confirmReservation(result, datePicker, spinnerHours));
    }

    private void confirmReservation(String serverData, DatePicker datePicker, Spinner spinnerHours) {
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getTimeSlot.php?token="
                + TokenManager.getToken();
        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            String selectedDate = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
            String selectedTime = spinnerHours.getSelectedItem().toString();
            int[] slotInfo = getSlotIdNMaxWatt(serverData, selectedDate, selectedTime);
            int slotId = slotInfo[0];
            int maxWattage = slotInfo[1];
            if (slotId != -1) {
                confirmReservation(slotId, maxWattage);
            } else {
                confirmReservation(slotId, maxWattage);
            }
        });
    }

    private void confirmReservation(int slotId, int slotMaxWattage) {
        double totalConsumption = calculateTotalConsumption() +
                calculateAlreadyBookedConsumption(slotId);

        if (totalConsumption > slotMaxWattage) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(getString(R.string.txt_confirm_reservation));
            builder.setMessage(getString(R.string.txt_consumption_exceeds));
            builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                actuateCoins(-5);
                storeReservationInDatabase(slotId);
                Toast.makeText(requireContext(), getString(R.string.txt_reservation_is_confirm),
                        Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            });
            builder.setNegativeButton(getString(R.string.no), (dialog, which) -> {
                actuateCoins(10);
                Toast.makeText(requireContext(), getString(R.string.txt_reservation_is_cancelled),
                        Toast.LENGTH_SHORT).show();
            });
            builder.show();
        } else {
            storeReservationInDatabase(slotId);
            Toast.makeText(requireContext(), getString(R.string.txt_reservation_is_confirm), Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        }
    }

    private double calculateTotalConsumption() {
        double totalConsumption = 0.0;
        for (Appliance appliance : appliances) {
            totalConsumption += appliance.getWattage();
        }
        return totalConsumption;
    }

    private double calculateAlreadyBookedConsumption(int slotId) {
        double[] consumption = {0.0};

        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getAppliancesInTimeSlot.php?slotId=" + slotId;

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    double wattage = jsonObject.getDouble("wattage");
                    consumption[0] += wattage;
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        });

        return consumption[0];
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

    private int[] getSlotIdNMaxWatt(String serverData, String selectedDate, String selectedTime) {
        int[] result = new int[2];
        try {
            JSONArray jsonArray = new JSONArray(serverData);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String hour = selectedTime.split("h")[0];
            Date selectedDateTime = dateFormat.parse(selectedDate + " " + hour + ":00:00");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String begin = jsonObject.getString("begin");
                Date beginDate = dateFormat.parse(begin);
                assert selectedDateTime != null;
                if (selectedDateTime.equals(beginDate)) {
                    result[0] = Integer.parseInt(jsonObject.getString("id"));
                    result[1] = Integer.parseInt(jsonObject.getString("max_wattage"));
                    return result;
                }
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return new int[]{-1,-1};
    }

    private void storeReservationInDatabase(int slotId) {
        String urlString =
                "http://remi-lem.alwaysdata.net/gestionResidence/postBookedTimeSlot.php?token="
                        + TokenManager.getToken() + "&appliance_id=" + appliances.get(0).getId()
                        + "&time_slot_id=" + slotId + "&order=" + order;

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            if(result == null)
                Log.d(TAG, "No response from the server!!!");
        });
        ++order;
    }
}