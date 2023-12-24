package com.example.sijili.users.clientactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sijili.R;
import com.example.sijili.RetrofitInterface;
import com.example.sijili.TermsAndConditionsActivity;
import com.example.sijili.requests.BusinessRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientHomeSubmitRequestActivity extends AppCompatActivity {
    EditText companyNameEditText, addressEditText, phoneNumberEditText, activityTypeEditText, dateOfBirthEditText, nationalityEditText, nationalityNumEditText;
    CheckBox verifiedCheckBox;
    Button buttonCT;
    DrawerLayout drawerLayout;
    private String BASE_URL = "http://192.168.1.41:4000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_submit_request);


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

    public void handleCn(View view) {
        Intent intent = new Intent(ClientHomeSubmitRequestActivity.this, TermsAndConditionsActivity.class);
        startActivity(intent);
    }

    public void submitRequestHandler(View view) {
        // Gather information from UI elements
        companyNameEditText.findViewById(R.id.enteredCompanyName);
        addressEditText.findViewById(R.id.enteredAddress);
        phoneNumberEditText.findViewById(R.id.enteredPhoneNumber);
        activityTypeEditText.findViewById(R.id.enteredActivityType);
        dateOfBirthEditText.findViewById(R.id.editDate);
        nationalityEditText.findViewById(R.id.enteredNationality);
        nationalityNumEditText.findViewById(R.id.enteredNumId);
        verifiedCheckBox.findViewById(R.id.verifiedBtn);

        // Get values from UI elements
        String companyName = companyNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String activityType = activityTypeEditText.getText().toString();
        String dateOfBirth = dateOfBirthEditText.getText().toString();
        String nationality = nationalityEditText.getText().toString();
        String nationalityNum = nationalityNumEditText.getText().toString();
        boolean isDeclared = verifiedCheckBox.isChecked();

        SharedPreferences preferences = getSharedPreferences("YourPreferencesName", Context.MODE_PRIVATE);
        String authToken = preferences.getString("token", "");

        BusinessRequest businessRequest = new BusinessRequest(
                companyName, address, phoneNumber, activityType,
                dateOfBirth, nationality, nationalityNum, isDeclared
        );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<Void> call = retrofitInterface.executeNewRequest(businessRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    Toast.makeText(ClientHomeSubmitRequestActivity.this, "Request submitted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(ClientHomeSubmitRequestActivity.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Toast.makeText(ClientHomeSubmitRequestActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
