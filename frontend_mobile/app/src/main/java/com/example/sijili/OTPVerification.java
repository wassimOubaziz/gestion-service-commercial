package com.example.sijili;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sijili.requests.EmailRequest;
import com.example.sijili.requests.OTPRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPVerification extends BaseActivity {
    private EditText digit1, digit2, digit3, digit4;
    private RetrofitInterface retrofitInterface;
    String email = "";
    String emailForPass = "";
    Intent i;

    private String BASE_URL = "http://192.168.43.59:4000";
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        i = getIntent();
        email = i.getStringExtra("email"); // Replace with the actual email
        emailForPass = i.getStringExtra("emailForPass");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        digit1 = findViewById(R.id.otp1);
        digit2 = findViewById(R.id.otp2);
        digit3 = findViewById(R.id.otp3);
        digit4 = findViewById(R.id.otp4);

        digit1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        digit2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        digit3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        digit4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        setEditTextListeners();
    }

    private void setEditTextListeners() {
        digit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    digit2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        digit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    digit3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        digit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    digit4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void verifyDigits(View view) {
        String sourceActivity = i.getStringExtra("sourceActivity");
        String otp = digit1.getText().toString() +
                digit2.getText().toString() +
                digit3.getText().toString() +
                digit4.getText().toString();


        if ("ForgotPasswordActivity".equals(sourceActivity)){
            emailForPass = i.getStringExtra("emailForPass");
            OTPRequest otpRequest = new OTPRequest(emailForPass, otp);
            showLoadingDialog();
            Call<Void> call = retrofitInterface.verifyOtp(otpRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    dismissLoadingDialog();

                    if (response.isSuccessful()) {
                        // Handle successful verification
                        Toast.makeText(OTPVerification.this, "Verification successful", Toast.LENGTH_SHORT).show();
                        // Proceed to the next step, e.g., redirect to a new activity
                        Intent intent = new Intent(OTPVerification.this, CreateNewPassword.class);
                        intent.putExtra("emailForPass", emailForPass);
                        startActivity(intent);
                        finish();
                    } else {
                        // Handle unsuccessful verification
                        Toast.makeText(OTPVerification.this, "Verification failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    dismissLoadingDialog();
                    // Handle network errors or other failures
                    Toast.makeText(OTPVerification.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
            return;

        }
        OTPRequest otpRequest = new OTPRequest(email, otp);
        showLoadingDialog();
        Call<Void> call = retrofitInterface.verifyOtp(otpRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dismissLoadingDialog();
                if (response.isSuccessful()) {
                    // Handle successful verification
                    Toast.makeText(OTPVerification.this, "Verification successful", Toast.LENGTH_SHORT).show();
                    // Proceed to the next step, e.g., redirect to a new activity
                    Intent intent = new Intent(OTPVerification.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Handle unsuccessful verification
                    Toast.makeText(OTPVerification.this, "Verification failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dismissLoadingDialog();
                // Handle network errors or other failures
                Toast.makeText(OTPVerification.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void send_again_pass_btn(View view) {
        emailForPass = i.getStringExtra("emailForPass");
        Log.d("testEmail", emailForPass);
        EmailRequest emailRequest = new EmailRequest(emailForPass);
        showLoadingDialog();
        Call<Void> call = retrofitInterface.forgotPassword(emailRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dismissLoadingDialog();
                if (response.isSuccessful()) {
                    // Handle successful initiation of forgot password process
                    Toast.makeText(OTPVerification.this, "Validation code sent successfully", Toast.LENGTH_SHORT).show();

                } else {
                    // Handle unsuccessful initiation of forgot password process
                    Toast.makeText(OTPVerification.this, "Failed to initiate forgot password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dismissLoadingDialog();
                // Handle network errors or other failures
                Toast.makeText(OTPVerification.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
//        intent.putExtra("emailForPass", email.getText().toString());

    }
}