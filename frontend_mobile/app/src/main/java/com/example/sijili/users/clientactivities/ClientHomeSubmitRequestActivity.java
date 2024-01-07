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
import android.util.Log;
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

import com.example.sijili.BaseActivity;
import com.example.sijili.R;
import com.example.sijili.RetrofitInterface;
import com.example.sijili.TermsAndConditionsActivity;
import com.example.sijili.other.DialogUtils;
import com.example.sijili.other.NavigationUtil;
import com.example.sijili.requests.BusinessRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientHomeSubmitRequestActivity extends BaseActivity {
    EditText companyNameEditText, addressEditText, phoneNumberEditText, activityTypeEditText, dateOfBirthEditText, nationalityEditText, nationalityNumEditText;
    CheckBox verifiedCheckBox;
    private String BASE_URL = "http://192.168.140.221:4000";
    private Retrofit retrofit;

    private RetrofitInterface retrofitInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_submit_request);
        companyNameEditText = findViewById(R.id.enteredCompanyName);
        addressEditText = findViewById(R.id.enteredAddress);
        phoneNumberEditText = findViewById(R.id.enteredPhoneNumber);
        activityTypeEditText = findViewById(R.id.enteredActivityType);
        dateOfBirthEditText = findViewById(R.id.editDate);
        nationalityEditText = findViewById(R.id.enteredNationality);
        nationalityNumEditText = findViewById(R.id.enteredNumId);
        verifiedCheckBox = findViewById(R.id.verifiedBtn);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        setupNavigationDrawer();


    }

    public void handleCn(View view) {
        Intent intent = new Intent(ClientHomeSubmitRequestActivity.this, TermsAndConditionsActivity.class);
        startActivity(intent);
    }

    public void submitRequestHandler(View view) {
        // Gather information from UI elements
        int requestLimit = 2;
        /*int currentRequestCount = getRequestCount();
        if (currentRequestCount >= requestLimit) {
            Toast.makeText(this, "You have reached the maximum limit of allowed requests.", Toast.LENGTH_SHORT).show();
            return;
        }*/
        // Get values from UI elements
        String companyName = companyNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String activityType = activityTypeEditText.getText().toString();
        String dateOfBirth = dateOfBirthEditText.getText().toString();
        String nationality = nationalityEditText.getText().toString();
        String nationalityNum = nationalityNumEditText.getText().toString();
        boolean isDeclared = verifiedCheckBox.isChecked();
        if (companyName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() ||
                activityType.isEmpty() || dateOfBirth.isEmpty() || nationality.isEmpty() ||
                nationalityNum.isEmpty()) {
            DialogUtils.showWarningDialog(this, getString(R.string.empty_field_warning));
            return;
        }

        if (!phoneNumber.matches("^(05|06|07)\\d{8}$")) {
            DialogUtils.showWarningDialog(this, getString(R.string.invalid_phone_number_warning));
            return;
        }
        if (!nationalityNum.matches( "^\\d{9}$")) {
            DialogUtils.showWarningDialog(this, getString(R.string.invalid_national_id_warning));
            return;
        }
        if (!dateOfBirth.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            DialogUtils.showWarningDialog(this, getString(R.string.invalid_date_of_birth_warning));
            return;
        }
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String authToken = preferences.getString("token", "");
        String authTokenHeader = "Bearer " + authToken;
        BusinessRequest businessRequest = new BusinessRequest(
                companyName, address, phoneNumber, activityType,
                dateOfBirth, nationality, nationalityNum, isDeclared
        );

        showLoadingDialog();
        Call<Void> call = retrofitInterface.executeNewRequest(authTokenHeader, businessRequest);

        Log.d("SubmitRequestHandler", "Retrofit Request: " + call.request().toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dismissLoadingDialog();
                Log.d("SubmitRequestHandler", "Response :" + response);
                if (response.isSuccessful()) {
                    // Handle successful response
                    Toast.makeText(ClientHomeSubmitRequestActivity.this, "Request submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(ClientHomeSubmitRequestActivity.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dismissLoadingDialog();
                // Handle failure
                Toast.makeText(ClientHomeSubmitRequestActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
        incrementRequestCount();
    }
    public void onUserButtonClick(View view) {
        NavigationUtil.navigateToProfile(this);
    }



    /*
   ===========================================
   Handling dialogue function
   ===========================================
     */
    private int getRequestCount() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getInt("requestCount", 0);
    }
    private void incrementRequestCount() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        int currentCount = preferences.getInt("requestCount", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("requestCount", currentCount + 1);
        editor.apply();
    }





}
