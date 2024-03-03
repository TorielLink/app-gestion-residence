package iut.dam.gestionresidence.ui.fragments.about;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import iut.dam.gestionresidence.R;

public class AboutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.txt_about)
                .setPositiveButton("OK", (dialog, id) -> {
                    // Click listener for OK button, do nothing, dialog will be dismissed
                });
        return builder.create();
    }
}

