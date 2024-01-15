package com.example.sijili.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.sijili.BaseActivity;
import com.example.sijili.R;
import com.example.sijili.messagerie.ChatActivity;
import com.example.sijili.messagerie.SocketHandler;
import com.example.sijili.other.NavigationUtil;
import com.example.sijili.users.clientactivities.ClientFollowRequestActivity;
import com.example.sijili.users.clientactivities.ClientHomeSubmitRequestActivity;
import com.example.sijili.users.clientactivities.RessourcesActivity;
import com.google.android.material.navigation.NavigationView;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import io.socket.client.Socket;

public class ClientHomeActivity extends BaseActivity {
    private Socket socket;
    private TextView welcomeText;

    //    private WebSocketClients webSocketClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        displayClientName();

        // Setup navigation drawer
        setupNavigationDrawer();

        //added route

    }


    private void displayClientName() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String name = preferences.getString("name", "");
        welcomeText = findViewById(R.id.welcomeClientText);
        welcomeText.setText(welcomeText.getText().toString()+" "+name);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.disconnect();
        }
    }


    public void goToSubmitForm(View view) {
        Intent intent = new Intent(ClientHomeActivity.this, ClientHomeSubmitRequestActivity.class);
        startActivity(intent);
    }

    public void goToRequestProcess(View view) {
        Intent intent = new Intent(ClientHomeActivity.this, ClientFollowRequestActivity.class);
        startActivity(intent);
    }

    public void goToRessources(View view) {
        Intent intent = new Intent(ClientHomeActivity.this, RessourcesActivity.class);
        startActivity(intent);
    }


    public void ChangeToMessagery(View view) {
        Intent intent = new Intent(ClientHomeActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    public void onUserButtonClick(View view) {
        NavigationUtil.navigateToProfile(this);
    }

}