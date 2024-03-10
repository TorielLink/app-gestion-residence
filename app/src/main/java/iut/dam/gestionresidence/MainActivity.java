package iut.dam.gestionresidence;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import iut.dam.gestionresidence.databinding.ActivityMainBinding;
import iut.dam.gestionresidence.entities.TokenManager;
import iut.dam.gestionresidence.ui.fragments.about.AboutDialogFragment;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iut.dam.gestionresidence.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_user_account, R.id.nav_my_habitat, R.id.nav_list_habitats, R.id.nav_my_notifications,
                R.id.nav_my_preferences).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController,
                mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);

        getBundleInfosAndUpdate(headerView);

        setImgUser(headerView);
    }

    private void getBundleInfosAndUpdate(View headerView) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String email = bundle.getString("email");
            String token = bundle.getString("token");

            TokenManager.setToken(token);

            if (email != null) {
                TextView txtMail = headerView.findViewById(R.id.txt_user_email);
                txtMail.setText(email);
            }
            TextView txtName = headerView.findViewById(R.id.txt_user_name);
            setUserNameFromToken(token, txtName);

        } else {
            Log.e(TAG, "Intent extras bundle is null");
        }
    }

    private static void setImgUser(View headerView) {
        ImageView imgUser = headerView.findViewById(R.id.img_user_profile);
        imgUser.setImageResource(R.drawable.user_logo);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        assert key != null;
        if (key.equals("pref_key_language")) {
            String language = sharedPreferences.getString(key, "fr");
            setLocale(language);
            recreate(); // Reload the activity to apply the language change
        } else if (key.equals("pref_key_theme")) {
            String theme = sharedPreferences
                    .getString("pref_key_theme", "light");
            if (theme.equals("dark")) {
                setTheme(R.style.Theme_GestionResidence_Dark);
            } else {
                setTheme(R.style.Theme_GestionResidence);
            }
            recreate();
        }
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    private void setUserNameFromToken(String token, TextView textView){
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getUserName.php?token="+token;
        Ion.with(this).load(urlString).asString().setCallback((e, result) -> {
            String username;
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(result);
                String firstname = jsonObject.getString("firstname");
                String lastname = jsonObject.getString("lastname");
                username = firstname + " " + lastname;
            } catch (JSONException ex) {
                username = "GenericName";
            }
            textView.setText(username);
        });
    }
}
