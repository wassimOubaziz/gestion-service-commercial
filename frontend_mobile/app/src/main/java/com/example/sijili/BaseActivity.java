package com.example.sijili;

import android.content.Context;
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
import android.widget.LinearLayout;
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
import com.example.sijili.other.NotificationDialogFragment;
import com.example.sijili.requests.CommerceRequest;
import com.example.sijili.requests.NotificationsRequest;
import com.example.sijili.users.ClientHomeActivity;
import com.example.sijili.users.ServerHomeActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawerLayout;

    private String BASE_URL = "http://192.168.43.59:4000";
    protected NavigationView navigationView;
    private RetrofitInterface retrofitInterface;
    private Retrofit retrofit;
    private AlertDialog loadingDialog;
    private ImageButton notificationButton;
    private LinearLayout lightModeButton, darkModeButton, followSystemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fetchNotificationSeenStatus();
    }
    public void openNavigationMenu() {
        // Open the navigation menu
        drawerLayout.openDrawer(GravityCompat.START);
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
            showThemeSelectionDialog();
        } else if (id == R.id.nav_languages) {
            showLanguageSelectionDialog();
        } else if (id == R.id.nav_rate_us) {
            showRatingDialog();
        } else if (id == R.id.nav_logout) {
            logoutHandler();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void fetchNotificationSeenStatus() {
        // Get user token from SharedPreferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String authToken = "Bearer " + preferences.getString("token", "");

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create RetrofitInterface
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Make a network request
        Call<Boolean> call = retrofitInterface.getNotificationSeenStatus(authToken);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean notificationSeenStatus = response.body();
                    // Process the notificationSeenStatus
                    if (notificationSeenStatus != null) {
                        // Do something with the notificationSeenStatus
                        if (notificationSeenStatus){
                            notificationButton = findViewById(R.id.notif_btn);
                            notificationButton.setImageResource(R.drawable.notification_bell_black);
                        }else{
                            notificationButton = findViewById(R.id.notif_btn);
                            notificationButton.setImageResource(R.drawable.notification_black);
                        }
                    } else {
                        // Handle null response

                    }
                } else {
                    // Handle error

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("NetworkError", t.getMessage());
                // Handle failure
                showToast("Network error");
            }
        });
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

                    retrieveNotificationsFromServerOrLocal();
                }
            });
        }
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void retrieveNotificationsFromServerOrLocal() {
        // Get user ID from SharedPreferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", "");
        String authToken = "Bearer " + preferences.getString("token", "");

        // Create Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create RetrofitInterface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Make a network request
        showLoadingDialog();
        Call<List<NotificationsRequest>> call = retrofitInterface.getNotifications(authToken);
        call.enqueue(new Callback<List<NotificationsRequest>>() {
            @Override
            public void onResponse(Call<List<NotificationsRequest>> call, Response<List<NotificationsRequest>> response) {
                dismissLoadingDialog();
                if (response.isSuccessful()) {
                    List<NotificationsRequest> userNotifications = response.body();
                    // Process the list of NotificationsRequest objects
                    showNotificationDialog(userNotifications);
                } else {
                    // Handle error
                    showNotificationDialog(null);

                }
            }

            @Override
            public void onFailure(Call<List<NotificationsRequest>> call, Throwable t) {
                dismissLoadingDialog();
                Log.e("NetworkError", t.getMessage());
                showNotificationDialog(null);
                // Handle failure
                showToast("Network error");
            }
        });
    }


    private void showNotificationDialog(List<NotificationsRequest> notifications) {
        List<String> notificationMessages = new ArrayList<>();
        List<String> notificationTimestamp = new ArrayList<>();
        if (notifications != null && !notifications.isEmpty()) {
            for (NotificationsRequest notification : notifications) {
                notificationMessages.add(notification.getMessage());
                notificationTimestamp.add(notification.getTimestamp());
                //notificationMessages.add(notification.getTimestamp());
            }
        } else {
            notificationMessages.add("No notifications");
        }

        NotificationDialogFragment dialogFragment = new NotificationDialogFragment(notificationMessages, notificationTimestamp);
        dialogFragment.show(getSupportFragmentManager(), "notification_dialog");
    }

//    home

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

        LinearLayout btnEnglish = view.findViewById(R.id.btnEnglish);
        LinearLayout btnFrench = view.findViewById(R.id.btnFrench);
        builder.setTitle("Select A Language");
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

        // Inflate the custom layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.theme_selection_dialog, null);
        builder.setView(view)
                .setTitle("Select Your Mode");

        // Find the buttons in the custom layout
        lightModeButton = view.findViewById(R.id.lightModeButton);
        darkModeButton = view.findViewById(R.id.darkModeButton);
        followSystemButton = view.findViewById(R.id.followSystemButton);

        // Set click listeners for the buttons
        AlertDialog dialog = builder.create();

        lightModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switchThemeMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        darkModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switchThemeMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        followSystemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switchThemeMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        });


//        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void switchThemeMode(int selectedMode) {
        int themeMode;
        switch (selectedMode) {
            case AppCompatDelegate.MODE_NIGHT_NO: // Light Mode
                themeMode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case AppCompatDelegate.MODE_NIGHT_YES: // Dark Mode
                themeMode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM: // Follow System
                themeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
            default:
                return;
        }

        // Apply the selected theme mode
        AppCompatDelegate.setDefaultNightMode(themeMode);
    }

    private void switchAccount() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Ensure retrofit is not null before using it


        //delete device identifier
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Ensure retrofitInterface is not null before using it


        String authToken = "Bearer " + preferences.getString("token", "");

        retrofitInterface.switchRole(authToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                logoutHandler();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("LOGOUT", "Error logging out: " + t.getMessage());
                showToast("Network error");
            }
        });
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



    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        recreate();
    }
    private void showThanksDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thanks for your rating!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void logoutHandler() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Ensure retrofit is not null before using it


        //delete device identifier
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Ensure retrofitInterface is not null before using it


        String authToken = "Bearer " + preferences.getString("token", "");

        // Ensure authToken is not null before using it
        if (authToken == null) {
            // Handle the situation where authToken is null
            Log.e("LOGOUT", "Auth token is null");
            return;
        }

        retrofitInterface.logout(authToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                // Remove token and redirect
                editor.remove("token");
                editor.apply();

                // Redirect to the login screen
                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("LOGOUT", "Error logging out: " + t.getMessage());
                showToast("Network error");
            }
        });
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
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
