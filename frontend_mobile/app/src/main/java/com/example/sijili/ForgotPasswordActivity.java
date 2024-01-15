package com.example.sijili;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sijili.requests.EmailRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends BaseActivity {
    EditText email;
    private String BASE_URL = "http://192.168.43.59:4000";

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        email = findViewById(R.id.enteredEmailForPass);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    public void verifyOTP(View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this, OTPVerification.class);

        EmailRequest emailRequest = new EmailRequest(email.getText().toString());
        showLoadingDialog();
        Call<Void> call = retrofitInterface.forgotPassword(emailRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("testt", response+"");
                Log.d("testt", response.body()+"");
                Log.d("testt", response.errorBody()+"");
                dismissLoadingDialog();
                if (response.isSuccessful()) {
                    // Handle successful initiation of forgot password process
                    Toast.makeText(ForgotPasswordActivity.this, "Validation code sent successfully", Toast.LENGTH_SHORT).show();
                    intent.putExtra("sourceActivity", "ForgotPasswordActivity");
                    intent.putExtra("emailForPass", email.getText().toString());
                    startActivity(intent);
                    // Proceed to the next step, e.g., show UI for entering the validation code
                } else {
                    // Handle unsuccessful initiation of forgot password process
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to initiate forgot password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dismissLoadingDialog();
                // Handle network errors or other failures
                Toast.makeText(ForgotPasswordActivity.this, "Network error" , Toast.LENGTH_SHORT).show();
            }
        });
//        intent.putExtra("emailForPass", email.getText().toString());

    }

    public void goToBackLogin(View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}