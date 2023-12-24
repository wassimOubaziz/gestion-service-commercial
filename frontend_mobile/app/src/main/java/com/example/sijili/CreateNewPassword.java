package com.example.sijili;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreateNewPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);
    }

    public void passToVerifiedSection(View view) {
        Intent intent = new Intent(CreateNewPassword.this, PasswordChanged.class);
        startActivity(intent);
        finish();
    }
}