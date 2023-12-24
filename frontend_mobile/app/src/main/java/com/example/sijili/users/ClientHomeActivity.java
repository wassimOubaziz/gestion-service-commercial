package com.example.sijili.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import com.example.sijili.R;
import com.example.sijili.messagerie.ChatActivity;
import com.example.sijili.users.clientactivities.ClientFollowRequestActivity;
import com.example.sijili.users.clientactivities.ClientHomeSubmitRequestActivity;
import com.example.sijili.users.clientactivities.RessourcesActivity;

import android.view.View;
import android.widget.ImageButton;

import io.socket.client.Socket;

public class ClientHomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    private Socket socket;

    //    private WebSocketClients webSocketClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        // Find the DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Find the toolbar and menu button
        ImageButton menuButton = findViewById(R.id.menu_btn);

        // Set OnClickListener for the menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the navigation menu
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.disconnect();
        }
    }

    private String name;

    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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
}