package com.example.sijili;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sijili.requests.SignupRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class SignUpActivity extends BaseActivity {
    private EditText firstnameEditText, lastnameEditText, emailEditText, passwordEditText;

    private String BASE_URL = "http://192.168.43.59:4000";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        firstnameEditText = findViewById(R.id.enteredFirstname);
        lastnameEditText = findViewById(R.id.enteredLastname);
        emailEditText = findViewById(R.id.enteredEmailRegis);
        passwordEditText = findViewById(R.id.enteredPasswordRegis);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }
    public void signupHandler(View view) {
        String firstName = firstnameEditText.getText().toString().trim();
        String lastName = lastnameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a SignupRequest object to send to the server
        SignupRequest signupRequest = new SignupRequest(firstName, lastName, email, password);

        showLoadingDialog();
        // Call the sign-up API endpoint
        Call<Void> call = retrofitInterface.executeSignup(signupRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dismissLoadingDialog();
                if (response.isSuccessful()) {
                    // Handle successful sign-up response
                    Toast.makeText(SignUpActivity.this, "Sign-up successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, OTPVerification.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish(); // You may navigate to another activity upon successful sign-up
                } else {
                    // Handle unsuccessful sign-up response
                    Toast.makeText(SignUpActivity.this, "Sign-up failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dismissLoadingDialog();
                // Handle network errors or other failures
                Toast.makeText(SignUpActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}