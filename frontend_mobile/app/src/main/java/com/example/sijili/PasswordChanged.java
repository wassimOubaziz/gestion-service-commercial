package com.example.sijili;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PasswordChanged extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_changed);
    }

    public void backToLogin(View view) {
        Intent intent = new Intent(PasswordChanged.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}