package com.example.sijili.users.serveractivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.sijili.BaseActivity;
import com.example.sijili.R;
import com.example.sijili.RetrofitInterface;
import com.example.sijili.requests.BusinessRequest;
import com.example.sijili.requests.CommerceRequest;
import com.example.sijili.users.clientactivities.ClientFollowRequestInfos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerRequestInfos extends BaseActivity {
    private String BASE_URL = "http://192.168.140.221:4000";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private EditText idTextView;
    private EditText companyNameTextView;
    private EditText addressTextView;
    private EditText phoneNumberTextView;
    private EditText activityTypeTextView;
    private EditText birthDateTextView;
    private EditText nationalityTextView;
    private EditText numIDTextView;
    private EditText nameTextView;
    private EditText statusTextView;
    private Switch paySwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_request_infos);
        setupNavigationDrawer();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        idTextView = findViewById(R.id.currentId);
        companyNameTextView = findViewById(R.id.currentCompanyName);
        addressTextView = findViewById(R.id.currentAddress);
        nameTextView = findViewById(R.id.currentName);
        phoneNumberTextView = findViewById(R.id.currentPhoneNumber);
        activityTypeTextView = findViewById(R.id.currentActivityType);
        birthDateTextView = findViewById(R.id.currentBirthDate);
        nationalityTextView = findViewById(R.id.currentNationality);
        numIDTextView = findViewById(R.id.currentNumID);
        statusTextView = findViewById(R.id.currentStatus);
        paySwitch = findViewById(R.id.currentPay);


        // Retrieve the requestId from the intent
        String requestId = getIntent().getStringExtra("requestId2");

        // Fetch the details of the selected BusinessRequest using requestId
        fetchBusinessRequestDetails(requestId);
    }

    private void fetchBusinessRequestDetails(String requestId) {
        Toast.makeText(ServerRequestInfos.this, "Waiting", Toast.LENGTH_SHORT).show();

        // Assuming you have a RetrofitInterface method to get details by ID
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String authToken = "Bearer " + preferences.getString("token", "");
        showLoadingDialog();
        Call<CommerceRequest> call = retrofitInterface.getCommerceRequestById(authToken, requestId);
        call.enqueue(new Callback<CommerceRequest>() {
            @Override
            public void onResponse(Call<CommerceRequest> call, Response<CommerceRequest> response) {
                dismissLoadingDialog();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ServerRequestInfos.this, "Succefull", Toast.LENGTH_SHORT).show();

                    // Handle the retrieved BusinessRequest details
                    CommerceRequest commerceRequest = response.body();
                    idTextView.setText(commerceRequest.getId());
                    companyNameTextView.setText(commerceRequest.getCompanyName());
                    addressTextView.setText(commerceRequest.getAddress());
                    nameTextView.setText(commerceRequest.getName());
                    phoneNumberTextView.setText(commerceRequest.getPhoneNumber());
                    activityTypeTextView.setText(commerceRequest.getActivityType());
                    String formattedDate = formatBirthDate(commerceRequest.getDateOfBirth());
                    birthDateTextView.setText(formattedDate);
                    nationalityTextView.setText(commerceRequest.getNationality());
                    numIDTextView.setText(commerceRequest.getNationalityNum());
                    statusTextView.setText(commerceRequest.getStatus());
                    paySwitch.setChecked(commerceRequest.isPaid());

                    // Handle print btn based on status of payement
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(ServerRequestInfos.this, "Failed to fetch business request details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommerceRequest> call, Throwable t) {
                dismissLoadingDialog();
                // Handle failure
                Toast.makeText(ServerRequestInfos.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String formatBirthDate(String rawDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        try {
            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return rawDate; // Return the raw date if parsing fails
        }
    }
}