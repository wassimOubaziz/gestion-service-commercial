package com.example.sijili;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileInfos extends BaseActivity {
    EditText firsnameEditText, lastnameEditText, emailEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_infos);

        setupNavigationDrawer();
        displayUserInfos();
    }

    private void displayUserInfos() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String firstName = preferences.getString("firstName", "");
        String lastName = preferences.getString("lastName", "");
        String email = preferences.getString("email", "");

        firsnameEditText = findViewById(R.id.currentFirstName);
        lastnameEditText = findViewById(R.id.currentLastName);
        emailEditText = findViewById(R.id.currentEmail);

        firsnameEditText.setText(firstName);
        lastnameEditText.setText(lastName);
        emailEditText.setText(email);
    }


}