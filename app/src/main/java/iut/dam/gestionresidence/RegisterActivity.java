package iut.dam.gestionresidence;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Création de compte");
        setContentView(R.layout.activity_register);

        EditText etFirstName = findViewById(R.id.editTextFirstName);
        EditText etLastName = findViewById(R.id.editTextLastName);
        EditText etEmail = findViewById(R.id.editTextEmailRegister);
        EditText etPassword = findViewById(R.id.editTextPasswordRegister);

        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())){
                Toast.makeText(RegisterActivity.this, R.string.bad_syntax, Toast.LENGTH_SHORT).show();
                return;
            }

            String urlString = "http://remi-lem.alwaysdata.net/amenagor/signUp.php?firstName=" +
                    firstName + "&lastName=" + lastName + "&email=" + email + "&password=" +
                    password;
            Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
                if(result == null)
                    Log.d(TAG, "No response from the server!!!");
                else {
                    if(result.equals("OK")) {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.succesRegister))
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    dialog.dismiss();
                                    finish();
                                })
                                .setOnDismissListener(dialog -> finish())
                                .show();
                    }
                    else {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.errorRegister))
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
