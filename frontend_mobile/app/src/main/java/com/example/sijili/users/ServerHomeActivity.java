package com.example.sijili.users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sijili.BaseActivity;
import com.example.sijili.R;
import com.example.sijili.messagerie.SocketHandler;
import com.example.sijili.other.NavigationUtil;
import com.example.sijili.users.serveractivities.ServerManageRequestsActivity;
import com.example.sijili.users.serveractivities.ServerMessagesActivity;

public class ServerHomeActivity extends BaseActivity {
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);
        displayClientName();
        setupNavigationDrawer();


    }


    private void displayClientName() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String name = preferences.getString("name", "");
        welcomeText = findViewById(R.id.welcomeClientText);
        welcomeText.setText(welcomeText.getText().toString()+" "+name);
    }

    public void goToManageRequest(View view) {
        Intent intent = new Intent(ServerHomeActivity.this, ServerManageRequestsActivity.class);
        startActivity(intent);
    }

    public void goToMessagesList(View view) {
        Intent intent = new Intent(ServerHomeActivity.this, ServerMessagesActivity.class);
        startActivity(intent);
    }


    public void onUserButtonClick(View view) {
        NavigationUtil.navigateToProfile(this);
    }

}