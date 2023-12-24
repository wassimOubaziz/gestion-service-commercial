package com.example.sijili.users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.sijili.R;
import com.example.sijili.users.serveractivities.ServerManageRequestsActivity;
import com.example.sijili.users.serveractivities.ServerMessagesActivity;

public class ServerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);

        showToken();
    }


    public void goToManageRequest(View view) {
        Intent intent = new Intent(ServerHomeActivity.this, ServerManageRequestsActivity.class);
        startActivity(intent);
    }

    public void goToMessagesList(View view) {
        Intent intent = new Intent(ServerHomeActivity.this, ServerMessagesActivity.class);
        startActivity(intent);
    }
    private void showToken(){
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String authToken = preferences.getString("token", "");

        if (!authToken.isEmpty()) {
            // Show the authentication token in a Toast
            Toast.makeText(this, "Authentication Token: " + authToken, Toast.LENGTH_SHORT).show();
        }
    }
}