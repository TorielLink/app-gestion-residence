package iut.dam.gestionresidence.ui.fragments.list_habitats;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.adapter.HabitatAdapter;
import iut.dam.gestionresidence.databinding.FragmentListHabitatsBinding;
import iut.dam.gestionresidence.entities.Appliance;
import iut.dam.gestionresidence.entities.Habitat;
import iut.dam.gestionresidence.entities.TokenManager;
import iut.dam.gestionresidence.entities.User;

public class ListHabitatsFragment extends Fragment {

    private FragmentListHabitatsBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentListHabitatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView list = binding.listHabitats;
        TextView tvTotalWattage = binding.txtTotalWattage;

        ArrayList<Habitat> habitats = new ArrayList<>();

        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getHabitats.php?token="
                + TokenManager.getToken();

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            long totalWattage = 0;
            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject habitatJson = jsonArray.getJSONObject(i);
                    int habitatId = habitatJson.getInt("id");
                    int floor = habitatJson.getInt("floor");
                    double area = habitatJson.getDouble("area");
                    JSONArray applianceJsonArray = habitatJson.getJSONArray("appliances");
                    ArrayList<Appliance> habitatAppliances = new ArrayList<>();

                    for (int j = 0; j < applianceJsonArray.length(); j++) {
                        JSONObject applianceJson = applianceJsonArray.getJSONObject(j);
                        int applianceId = applianceJson.getInt("id");
                        String name = applianceJson.getString("name");
                        String reference = applianceJson.getString("reference");
                        int wattage = applianceJson.getInt("wattage");
                        totalWattage += wattage;
                        habitatAppliances.add(new Appliance(applianceId, name, reference, wattage));
                    }

                    habitats.add(new Habitat(habitatId, new User("User n°" + (i+1)), floor, area, habitatAppliances));
                }

                HabitatAdapter adapter = new HabitatAdapter(requireContext(), habitats);
                list.setAdapter(adapter);

                tvTotalWattage.setText(getString(R.string.total_wattage_habitats, String.valueOf(totalWattage)));

            } catch (JSONException ex) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.menu_list_habitats))
                        .setMessage(getString(R.string.error_get_habitats))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        HabitatAdapter adapter = new HabitatAdapter(requireContext(), habitats);
        list.setAdapter(adapter);

        Button btnHabitatRemove = binding.btnRemoveHabitat;
        EditText editTextIdHabitatRemove = binding.editTextIdHabitatRemove;

        btnHabitatRemove.setOnClickListener(view -> clickBtnHabitatRemove(editTextIdHabitatRemove));

        Button btnReserveTimeSlot = binding.btnReserveTimeSlot;

        btnReserveTimeSlot.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(requireActivity(),
                    R.id.nav_host_fragment_content_main);

            navController.navigate(R.id.nav_reservation);
        });

        return root;
    }

    private void clickBtnHabitatRemove(EditText editTextIdHabitatRemove) {
        int idRmHabitat = Integer.parseInt(editTextIdHabitatRemove.getText().toString().trim());

        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/removeHabitat.php?token="
                + TokenManager.getToken() + "&idHabitat=" + idRmHabitat;

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            if(result == null)
                Log.d(TAG, "No response from the server!!!");
            else {
                if(result.equals("OK")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.succes_remove_habitat))
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                            .show();
                }
                else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.error_remove_habitat))
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                            .show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}