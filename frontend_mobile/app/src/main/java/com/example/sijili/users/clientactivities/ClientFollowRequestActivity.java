package com.example.sijili.users.clientactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.sijili.ForgotPasswordActivity;
import com.example.sijili.LoginActivity;
import com.example.sijili.R;

public class ClientFollowRequestActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_follow_request);

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

    public void goToRequestInfos(View view) {
        Intent intent = new Intent(ClientFollowRequestActivity.this, ClientFollowRequestInfos.class);
        startActivity(intent);
    }
}
