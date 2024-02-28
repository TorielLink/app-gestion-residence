package iut.dam.gestionresidence.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import iut.dam.gestionresidence.R;
import iut.dam.gestionresidence.adapter.CountryAdapter;
import iut.dam.gestionresidence.components.Country;

public class LoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Page de connexion");
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.editTextTextEmailAddress);
        EditText etPassword = findViewById(R.id.editTextTextPassword);
        Button btnLogin = findViewById(R.id.button);
        Button btnCreateAcc = findViewById(R.id.button2);

        Spinner spinner = findViewById(R.id.spinner);

        btnLogin.setOnClickListener(view -> {
            String enteredEmail = etEmail.getText().toString().trim();
            String enteredPassword = etPassword.getText().toString().trim();

            if(android.util.Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()){
                if (enteredEmail.equals("admin@iut.fr") && enteredPassword.equals("admin")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email", enteredEmail);
                    bundle.putString("password", enteredPassword);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, R.string.wrongcredentials, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(LoginActivity.this, R.string.badsyntax, Toast.LENGTH_SHORT).show();
            }
        });

        btnCreateAcc.setOnClickListener(view -> {
            Intent myIntent = new Intent(this, RegisterActivity.class);
            startActivity(myIntent);
        });


        List<Country> items = new ArrayList<>();
        items.add(new Country(R.drawable.france_flag, "France"));
        items.add(new Country(R.drawable.sweden_flag, "Suede"));

        CountryAdapter adapter = new CountryAdapter(this, items);
        spinner.setAdapter(adapter);
    }
}
