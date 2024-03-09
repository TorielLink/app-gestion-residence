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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.koushikdutta.ion.Ion;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.databinding.FragmentMyHabitatBinding;
import iut.dam.gestionresidence.entities.TokenManager;


public class MyHabitatFragment extends Fragment {

    private FragmentMyHabitatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyHabitatViewModel homeViewModel =
                new ViewModelProvider(this).get(MyHabitatViewModel.class);

        binding = FragmentMyHabitatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText etFloor = binding.editTextFloor;
        EditText etArea = binding.editTextArea;
        Button btnAddHabitat = binding.btnAddHabitat;

        btnAddHabitat.setOnClickListener(view -> {
            int floor = Integer.parseInt(etFloor.getText().toString().trim());
            int area = Integer.parseInt(etArea.getText().toString().trim());
            String token = TokenManager.getToken();

            String urlString = "http://remi-lem.alwaysdata.net/amenagor/addHabitat.php?token="
                    + token + "&floor=" + floor + "&area=" + area;

            Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
                if(result == null)
                    Log.d(TAG, "No response from the server!!!");
                else {
                    if(result.equals("OK")) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.succesAddHabitat))
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .show();
                    }
                    else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.errorAddHabitat))
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .show();
                    }
                }
            });
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}