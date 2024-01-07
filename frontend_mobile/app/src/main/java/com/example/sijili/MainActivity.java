package com.example.sijili;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.sijili.users.ClientHomeActivity;
import com.example.sijili.users.ServerHomeActivity;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isLoggedIn()) {
            redirectToHomeScreen();
            return;
        }
//      setContentView(R.layout.activity_client_home_submit_request);
    }

    public void login(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private boolean isLoggedIn() {
        // Check if a valid authentication token is stored in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        return !token.isEmpty();
    }
    private void redirectToHomeScreen() {
        // Redirect to the home screen based on the user's role
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String role = preferences.getString("role", "");
        if (role.equals("client")) {
            Intent intent = new Intent(MainActivity.this, ClientHomeActivity.class);
            startActivity(intent);
        } else if (role.equals("server")) {
            Intent intent = new Intent(MainActivity.this, ServerHomeActivity.class);
            startActivity(intent);
        }
        finish();
    }
}