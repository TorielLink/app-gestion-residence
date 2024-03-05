package iut.dam.gestionresidence;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Page de connexion");
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.editTextEmailAddress);
        EditText etPassword = findViewById(R.id.editTextPassword);
        Button btnLogin = findViewById(R.id.btnLoginAccount);
        Button btnCreateAcc = findViewById(R.id.btnCreateAccount);
        Button btnGoogle = findViewById(R.id.btnConnectGoogle);
        Button btnForgotPwd = findViewById(R.id.btnForgottenPassword);

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
                    Toast.makeText(LoginActivity.this, R.string.wrong_credentials, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(LoginActivity.this, R.string.bad_syntax, Toast.LENGTH_SHORT).show();
            }
        });

        btnCreateAcc.setOnClickListener(view -> {
            Intent myIntent = new Intent(this, RegisterActivity.class);
            startActivity(myIntent);
        });

        btnForgotPwd.setOnClickListener(view -> {
            Intent myIntent = new Intent(this, RegisterActivity.class);
            startActivity(myIntent);
        });

        btnGoogle.setOnClickListener(view -> {
            // TODO
        });
    }
}
