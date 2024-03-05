package iut.dam.gestionresidence;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.koushikdutta.ion.Ion;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import iut.dam.gestionresidence.databinding.ActivityMainBinding;
import iut.dam.gestionresidence.ui.fragments.about.AboutDialogFragment;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //TODO : déléguer
        super.onCreate(savedInstanceState);

        iut.dam.gestionresidence.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_my_habitat, R.id.nav_list_habitats, R.id.nav_my_notifications,
                R.id.nav_my_preferences).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController,
                mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            View headerView = navigationView.getHeaderView(0);
            String email = bundle.getString("email");
            //String name = bundle.getString("name");
            if (email != null) {
                Log.d("debugRemi", email);
                TextView txtMail = headerView.findViewById(R.id.txt_user_email);
                txtMail.setText(email);
            }
            /*if (name != null) {
                TextView txtName = findViewById(R.id.txt_user_name);
                txtName.setText(name); //TODO : userName
            }*/
        } else {
            Log.e(TAG, "Intent extras bundle is null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            AboutDialogFragment aboutDialog = new AboutDialogFragment();
            aboutDialog.show(getSupportFragmentManager(), "about_dialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void getRemoteHabitats() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Getting list of habitats...").setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        String urlString = "http://remi-lem.alwaysdata.net/amenagor/getHabitats.php";
        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            alertDialog.dismiss();
            if(result == null)
                Log.d(TAG, "No response from the server!!!");
            else {
                //TODO : Traitement de result
            }
        });
    }

    public void addRemoteHabitat() {
        String urlString = "http://remi-lem.alwaysdata.net/amenagor/addHabitat.php?floor=X";
        Ion.with(this).load(urlString).asString().withResponse()
                .setCallback((e, response) -> {
            if(response.getHeaders().code() == 200) { /*TODO*/ }
            //TODO
        });
    }
}
