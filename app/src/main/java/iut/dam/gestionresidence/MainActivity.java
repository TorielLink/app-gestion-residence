package iut.dam.gestionresidence;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

import java.io.File;
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

        ImageView imgUser = headerView.findViewById(R.id.img_user_profile);
        setImgUser(imgUser, getApplicationContext());

        imgUser.setOnClickListener(view -> {
            navController.navigate(R.id.nav_user_account);
            drawer.closeDrawers();
        });
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

    private void setImgUser(ImageView imgUser, Context context) {
        trimCache(context);
        String urlString = "http://remi-lem.alwaysdata.net/gestionResidence/getProfilePicture.php?token="+TokenManager.getToken();
        Ion.with(this)
                .load(urlString)
                .asBitmap()
                .setCallback((e, bitmap) -> {
                    if (e == null && bitmap != null) {
                        imgUser.setImageBitmap(bitmap);
                    } else {
                        imgUser.setImageResource(R.drawable.user_logo);
                    }
                });
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
        switch (key) {
            case "pref_key_language":
                String language = sharedPreferences.getString(key, "fr");
                setLocale(language);
                recreate(); // Reload the activity to apply the language change

                break;
            case "pref_key_theme":
                String theme = sharedPreferences
                        .getString("pref_key_theme", "light");
                if (theme.equals("dark")) {
                    setTheme(R.style.Theme_GestionResidence_Dark);
                } else {
                    setTheme(R.style.Theme_GestionResidence);
                }
                recreate();
                break;
            case "pref_key_notification":
                boolean notificationsEnabled = sharedPreferences.getBoolean(key, false);
                if (notificationsEnabled) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        enableNotifications();
                    }
                } else {
                    disableNotifications();
                }
                break;
        }
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enableNotifications() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notif_channel_name);
            String description = getString(R.string.notif_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID", name,
                    importance);
            channel.setDescription(description);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_menu_third)
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(getString(R.string.notif_txt))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        assert notificationManager != null;
        notificationManager.notify(123, builder.build());
    }

    private void disableNotifications() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) notificationManager.cancelAll();
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
    public static void trimCache(Context context) {
        File dir = context.getCacheDir();
        if (dir != null && dir.isDirectory()) {
            deleteDir(dir);
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        assert dir != null;
        return dir.delete();
    }

}
