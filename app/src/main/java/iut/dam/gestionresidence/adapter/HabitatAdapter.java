package iut.dam.gestionresidence.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import iut.dam.gestionresidence.entities.Appliance;
import iut.dam.gestionresidence.entities.Habitat;
import iut.dam.gestionresidence.R;

public class HabitatAdapter extends ArrayAdapter<Habitat> {

    public HabitatAdapter(@NonNull Context context, @NonNull List<Habitat> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @SuppressLint("SetTextI18n")
    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_habitat, parent, false);
        }

        TextView residentTV = convertView.findViewById(R.id.residentTV);
        TextView applianceTV = convertView.findViewById(R.id.applianceTV);
        TextView numberFloorTV = convertView.findViewById(R.id.numberFloorTV);

        ArrayList<ImageView> appliancesList = new ArrayList<>();
        ImageView appliance1IV = convertView.findViewById(R.id.appliance1IV);
        appliancesList.add(appliance1IV);
        ImageView appliance2IV = convertView.findViewById(R.id.appliance2IV);
        appliancesList.add(appliance2IV);
        ImageView appliance3IV = convertView.findViewById(R.id.appliance3IV);
        appliancesList.add(appliance3IV);
        ImageView appliance4IV = convertView.findViewById(R.id.appliance4IV);
        appliancesList.add(appliance4IV);

        Habitat currentItem = getItem(position);

        if (currentItem != null) {
            residentTV.setText(currentItem.getName());
            numberFloorTV.setText(currentItem.getFloor());

            int nbAppliances = currentItem.getAppliances().size();

            for(ImageView i : appliancesList){
                i.setVisibility(View.INVISIBLE);
            }

            int nb = 0;
            for(ImageView i : appliancesList){
                nb++;
                if(nbAppliances >= nb){
                    // la base de données ne faisant pas correspondre l'ID à un équipement,
                    // nous sommes obligés de donner une image aléatoire à chaque équipement
                    i.setVisibility(View.VISIBLE);
                }
            }
            String txtAppliances = "";
            switch (nbAppliances) {
                case 0: txtAppliances = "Aucun équipement";
                    break;
                case 1: txtAppliances = "1 équipement";
                    break;
                default: txtAppliances = nbAppliances + " équipements";
            }
            applianceTV.setText(txtAppliances);
        }
        return convertView;
    }

    public static abstract class OnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           Log.d("MyApp","default click");
        }
    }
}
