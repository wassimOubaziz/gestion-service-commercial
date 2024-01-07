package com.example.sijili;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sijili.other.NavigationUtil;
import com.example.sijili.users.ClientHomeActivity;
import com.example.sijili.users.ServerHomeActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    private AlertDialog loadingDialog;
    private ImageButton notificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void setupNavigationDrawer() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String name = preferences.getString("name", "");
        String email = preferences.getString("email", "");
        int roleSize = preferences.getInt("roleSize", 0);
        // Find the DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Find the toolbar and menu button
        View menuButton = findViewById(R.id.menu_btn);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_name);
        TextView userEmailTextView = headerView.findViewById(R.id.user_email);
        userNameTextView.setText(name);
        userEmailTextView.setText(email);

        MenuItem professionalAccountItem = navigationView.getMenu().findItem(R.id.nav_professional_account);
        if (roleSize <= 1) {
            professionalAccountItem.setVisible(false);
        } else {
            professionalAccountItem.setVisible(true);
        }
        if (menuButton != null) {
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNavigationMenu();
                }
            });
        }

        notificationButton = findViewById(R.id.notif_btn);
        if (notificationButton != null) {
            notificationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click on notification button
                    // You can open a notification activity or show a custom view as per your requirement
                    // For example, you can show a Snackbar with the notification content
                    showNotificationSnackbar("This is a notification message.");
                }
            });
        }
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void showNotificationSnackbar(String message) {
        Snackbar    .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
    public void openNavigationMenu() {
        // Open the navigation menu
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            goToHome();
        } else if (id == R.id.nav_profile) {
            goToProfile();
        } else if (id == R.id.nav_professional_account) {
            switchAccount();

        } else if (id == R.id.nav_dark_mode) {
            showToast("Dark Mode selected");
            showThemeSelectionDialog();
        } else if (id == R.id.nav_languages) {
            showToast("Languages selected");
            showLanguageSelectionDialog();
        } else if (id == R.id.nav_rate_us) {
            showRatingDialog();
        } else if (id == R.id.nav_logout) {
            logoutHandler();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToHome() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String role = preferences.getString("role", "");

        // Check the current activity class
        Class<?> currentActivityClass = this.getClass();
        Class<?> targetActivityClass;

        if (role.equals("client")) {
            targetActivityClass = ClientHomeActivity.class;
        } else if (role.equals("server")) {
            targetActivityClass = ServerHomeActivity.class;
        } else {
            // Handle other roles or cases
            return;
        }

        // If the user is not already in the target activity, start it
        if (!currentActivityClass.equals(targetActivityClass)) {
            Intent intent = new Intent(BaseActivity.this, targetActivityClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the back stack
            startActivity(intent);
            finish();
        }
    }

    private void goToProfile() {
        // Check the current activity class
        Class<?> currentActivityClass = this.getClass();
        Class<?> targetActivityClass = ProfileInfos.class;

        // If the user is not already in the target activity, start it
        if (!currentActivityClass.equals(targetActivityClass)) {
            Intent intent = new Intent(BaseActivity.this, targetActivityClass);
            startActivity(intent);
        }
    }
    private void showLanguageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_language_selection, null);

        Button btnEnglish = view.findViewById(R.id.btnEnglish);
        Button btnFrench = view.findViewById(R.id.btnFrench);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
                dialog.dismiss();
            }
        });

        btnFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("fr");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showThemeSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Theme Mode")
                .setSingleChoiceItems(new CharSequence[]{"Light Mode", "Dark Mode", "Follow System"}, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switchThemeMode(which);
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
    private void switchThemeMode(int selectedMode) {
        int themeMode;
        switch (selectedMode) {
            case 0: // Light Mode
                themeMode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case 1: // Dark Mode
                themeMode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case 2: // Follow System
                themeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
            default:
                return;
        }

        AppCompatDelegate.setDefaultNightMode(themeMode);
        recreate(); // Recreate the activity to apply the new theme
    }
    private void switchAccount() {
        Class<?> currentActivityClass = this.getClass();
        Class<?> targetActivityClass;

        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String role = preferences.getString("role", "");

        // Determine the target activity class based on the current role
        if ("client".equals(role)) {
            targetActivityClass = ServerHomeActivity.class;
        } else if ("server".equals(role)) {
            targetActivityClass = ClientHomeActivity.class;
        } else {
            // Handle other roles or cases
            return;
        }

        // Check if the user is already in the target activity
        if (!currentActivityClass.equals(targetActivityClass)) {
            Intent intent = new Intent(BaseActivity.this, targetActivityClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_rate_us, null);
        RatingBar ratingBar = view.findViewById(R.id.rating_bar);

        builder.setTitle("Rate Us")
                .setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        float rating = ratingBar.getRating();
                        // TODO: Handle the rating (e.g., submit to a server, store locally)

                        // Show a "Thanks" message
                        showThanksDialog();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
    private void logoutHandler(){
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.apply();

        // Redirect to the login screen
        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void setLocale(String languageCode) {
        // Set the selected language code and recreate the activity
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        Resources resources = getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        recreate(); // Recreate the activity to apply the new locale
    }
    private void showThanksDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thanks!")
                .setMessage("Thanks for your rating!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    protected void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TransparentDialog);
        View view = getLayoutInflater().inflate(R.layout.activity_loading, null);
        builder.setView(view);
        builder.setCancelable(false);
        loadingDialog = builder.create();
        loadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
