package com.example.sijili;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sijili.other.LoadingActivity;
import com.example.sijili.users.ClientHomeActivity;
import com.example.sijili.users.ServerHomeActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.168.1.41:4000";
    private AlertDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.enteredEmail);
        passwordEditText = findViewById(R.id.enteredPassword);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    public void loginHandler(View view) {
        showLoadingScreen();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a LoginRequest object to send to the server
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Call the login API endpoint
        Call<LoginResponse> call = retrofitInterface.executeLogin(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideLoadingScreen();
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        String token = loginResponse.getToken();

                        // Save the token to SharedPreferences
                        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("token", token);

                        // Save the role information to SharedPreferences
                        List<String> roles = loginResponse.getRole();
                        if (roles != null && roles.size() > 0) {
                            editor.putString("role", roles.get(0));
                            editor.putInt("roleSize", roles.size());
                        }

                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Login successful. Token: " + token, Toast.LENGTH_SHORT).show();
                        if (roles.get(0).equals("client")) {
                            Intent intent = new Intent(LoginActivity.this, ClientHomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (roles.get(0).equals("server")) {
                            Intent intent = new Intent(LoginActivity.this, ServerHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // Handle unsuccessful login response
                        String errorMessage = loginResponse.getMessage();
                        System.out.println("*****asd" + errorMessage);
                        if (errorMessage != null && !errorMessage.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed1", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Handle unsuccessful login response
                    hideLoadingScreen();
                    Toast.makeText(LoginActivity.this, "Login failed2", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideLoadingScreen();
                // Handle network errors or other failures
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void forgotPassword(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoadingScreen() {
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
        finish();
    }

    private void hideLoadingScreen() {
        // Finish the LoadingActivity to hide the loading screen
        finishActivity(LoadingActivity.class.hashCode());
        finish();
    }
}
