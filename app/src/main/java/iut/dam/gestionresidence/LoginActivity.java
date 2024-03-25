package iut.dam.gestionresidence;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("581797190264-ugcr0748ekqoj1h4pu796uau1ub4n11v.apps.googleusercontent.com")//getString(R.string.default_client_id)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.btn_connect_google);
        signInButton.setOnClickListener(v -> signIn());

        EditText etEmail = findViewById(R.id.edit_text_email_address);
        EditText etPassword = findViewById(R.id.edit_text_password);
        Button btnLogin = findViewById(R.id.btn_login_account);
        Button btnCreateAcc = findViewById(R.id.btn_create_account);
        Button btnForgotPwd = findViewById(R.id.btn_forgotten_password);

        btnLogin.setOnClickListener(view -> {
            String enteredEmail = etEmail.getText().toString().trim();
            String enteredPassword = etPassword.getText().toString().trim();

            if(android.util.Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()){
                testPassword(enteredEmail, enteredPassword);
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
            Intent myIntent = new Intent(this, ForgottenPasswordActivity.class);
            startActivity(myIntent);
        });

    }
    private void testPassword(String email, String password){
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/login.php?email="+email+"&password="+password;
        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            if (result == null)
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.login_account))
                        .setMessage(getString(R.string.network_error))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                        .show();
            else {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    String token = jsonObject.getString("token");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    bundle.putString("token", token);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (JSONException ex) {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.login_account))
                            .setMessage(getString(R.string.account_inexistant))
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                            .show();
                }
            }
        });
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", "user@google.com");
                bundle.putString("token", "66fee3f7e925fc3e1333614dc1cf3360");
                intent.putExtras(bundle);
                startActivity(intent);

            });

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }
}
