package iut.dam.gestionresidence;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ForgottenPasswordActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Mot de passe oublié");
        setContentView(R.layout.activity_forgotten_password);
    }
}
