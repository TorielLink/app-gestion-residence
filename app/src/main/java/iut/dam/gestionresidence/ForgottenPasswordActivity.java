package iut.dam.gestionresidence;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgottenPasswordActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Mot de passe oublié");
        setContentView(R.layout.activity_forgotten_password);

        EditText etEmail = findViewById(R.id.editTextEmailPasswordForgotten);
        Button btnLogin = findViewById(R.id.btnPasswordForgotten);

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();

            String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getPassword.php?email="+email;
            Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
                if(result == null)
                    Log.d(TAG, "No response from the server!!!");
                else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        String password = jsonObject.getString("password");
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.password))
                                .setMessage(getString(R.string.password_message, password))
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    dialog.dismiss();
                                    finish();
                                })
                                .setOnDismissListener(dialog -> finish())
                                .show();
                    } catch (JSONException ex) {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.password))
                                .setMessage(getString(R.string.account_inexistant))
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    dialog.dismiss();
                                    finish();
                                })
                                .setOnDismissListener(dialog -> finish())
                                .show();
                    }
                }
            });
        });
    }
}
