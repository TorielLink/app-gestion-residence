package iut.dam.gestionresidence.ui.fragments.my_habitat;

import static android.content.ContentValues.TAG;

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
import androidx.lifecycle.ViewModelProvider;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.adapter.HabitatAdapter;
import iut.dam.gestionresidence.databinding.FragmentMyHabitatBinding;
import iut.dam.gestionresidence.entities.Appliance;
import iut.dam.gestionresidence.entities.Habitat;
import iut.dam.gestionresidence.entities.TokenManager;
import iut.dam.gestionresidence.entities.User;


public class MyHabitatFragment extends Fragment {

    private FragmentMyHabitatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyHabitatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText etFloor = binding.editTextFloor;
        EditText etArea = binding.editTextArea;
        Button btnAddHabitat = binding.btnAddHabitat;

        EditText etNameAppliance = binding.editTextApplianceName;
        EditText etReference = binding.editTextReference;
        EditText etWattage = binding.editTextWattage;
        Button btnAddAppliance = binding.btnAddAppliance;

        EditText etIdApplianceRm = binding.editTextIdApplianceRemove;
        Button btnRemoveAppliance = binding.btnRemoveAppliance;

        String token = TokenManager.getToken();

        btnAddHabitat.setOnClickListener(view -> {
            clickBtnAddHabitat(etFloor, etArea, token);
        });

        btnAddAppliance.setOnClickListener(view ->{
            clickBtnAddAppliance(etNameAppliance, etReference, etWattage, token);
        });

        btnRemoveAppliance.setOnClickListener(view ->{
            clickbtnRemoveAppliance(etIdApplianceRm, token);
        });

        getAppliances();

        return root;
    }

    private void clickbtnRemoveAppliance(EditText etIdApplianceRm, String token) {
        int idRmAppliance = Integer.parseInt(etIdApplianceRm.getText().toString().trim());

        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/removeAppliance.php?token="
                + token + "&idAppliance=" + idRmAppliance;

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            if(result == null)
                Log.d(TAG, "No response from the server!!!");
            else {
                if(result.equals("OK")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.succes_remove_appliance))
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
                else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.error_remove_appliance))
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
            }
        });
    }

    private void clickBtnAddAppliance(EditText etNameAppliance, EditText etReference, EditText etWattage, String token) {
        String name = etNameAppliance.getText().toString().trim();
        String ref = etReference.getText().toString().trim();
        int wattage = Integer.parseInt(etWattage.getText().toString().trim());

        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/addAppliance.php?token="
                + token + "&name=" + name + "&reference=" + ref + "&wattage=" + wattage;

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            if(result == null)
                Log.d(TAG, "No response from the server!!!");
            else {
                if(result.equals("OK")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.succes_add_appliance))
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
                else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.error_add_appliance))
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
            }
        });
    }

    private void clickBtnAddHabitat(EditText etFloor, EditText etArea, String token) {
        int floor = Integer.parseInt(etFloor.getText().toString().trim());
        int area = Integer.parseInt(etArea.getText().toString().trim());

        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/addHabitat.php?token="
                + token + "&floor=" + floor + "&area=" + area;

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            if(result == null)
                Log.d(TAG, "No response from the server!!!");
            else {
                if(result.equals("OK")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.succes_add_habitat))
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
                else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.error_add_habitat))
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
            }
        });
    }

    private void getAppliances() {
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getUserName.php?token="
                + TokenManager.getToken();
        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            String username;
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(result);
                String id_habitat = jsonObject.getString("habitat_id");
                if(id_habitat.equals("null")) {
                    //no data found in BD
                    return;
                }
                String firstname = jsonObject.getString("firstname");
                String lastname = jsonObject.getString("lastname");
                username = firstname + " " + lastname;
                showAppliancesForHabitat(Integer.parseInt(id_habitat), username);
            } catch (JSONException ex) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.error_get_habitats))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }

    private void showAppliancesForHabitat(int idHabitat, String username) {
        ListView list = binding.listHabitat;
        TextView tvTotalWattage = binding.txtTotalWattage;

        ArrayList<Habitat> habitats = new ArrayList<>();

        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getHabitats.php?token="
                + TokenManager.getToken();

        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            JSONObject jsonObject = null;
            long totalWattage = 0;
            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject habitatJson = jsonArray.getJSONObject(i);
                    int habitatId = habitatJson.getInt("id");
                    if(habitatId == idHabitat){
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

                        habitats.add(new Habitat(habitatId, new User(username), floor, area, habitatAppliances));
                        break;
                    }
                }

                HabitatAdapter adapter = new HabitatAdapter(requireContext(), habitats);
                list.setAdapter(adapter);

                tvTotalWattage.setText(getString(R.string.total_wattage_habitats, String.valueOf(totalWattage)));

            } catch (JSONException ex) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.menu_list_habitats))
                        .setMessage(getString(R.string.error_get_appliances))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });

        HabitatAdapter adapter = new HabitatAdapter(requireContext(), habitats);
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}