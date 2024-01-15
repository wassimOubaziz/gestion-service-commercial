package com.example.sijili;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sijili.requests.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileInfos extends BaseActivity {
    EditText firsnameEditText, lastnameEditText, emailEditText;
    private String BASE_URL = "http://192.168.43.59:4000";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_infos);
        firsnameEditText = findViewById(R.id.currentFirstName);
        lastnameEditText = findViewById(R.id.currentLastName);
        emailEditText = findViewById(R.id.currentEmail);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        setupNavigationDrawer();
        displayUserInfos();
    }

    private void displayUserInfos() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String firstName = preferences.getString("firstName", "");
        String lastName = preferences.getString("lastName", "");
        String email = preferences.getString("email", "");
        firsnameEditText.setText(firstName);
        lastnameEditText.setText(lastName);
        emailEditText.setText(email);
    }


    public void editContent(View view) {
        firsnameEditText.setFocusable(true);
        firsnameEditText.setFocusableInTouchMode(true);

        lastnameEditText.setFocusable(true);
        lastnameEditText.setFocusableInTouchMode(true);

        emailEditText.setFocusable(true);
        emailEditText.setFocusableInTouchMode(true);

        firsnameEditText.requestFocus();
    }

    public void saveContent(View view) {

        // Get the updated data from the EditText fields
        String firstname = firsnameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String authToken = "Bearer " + preferences.getString("token", "");
        // Assuming you have userId stored somewhere in your activity
        String userId = preferences.getString("userId", "");

        // Create a User object with the updated data
        User updatedUser = new User();
        updatedUser.setFirstname(firstname);
        updatedUser.setLastname(lastname);
        updatedUser.setEmail(email);

        // Make the API call to update the user
        showLoadingDialog();
        Call<User> call = retrofitInterface.saveUser(userId, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dismissLoadingDialog();
                if (response.isSuccessful()) {
                    // Handle success
                    recreate();
                    User savedUser = response.body();
                    // Handle the saved user object as needed
                } else {
                    // Handle error
                    // You can get error details from response.errorBody()
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dismissLoadingDialog();
                Toast.makeText(ProfileInfos.this, "Network error", Toast.LENGTH_SHORT).show();
                // Handle failure
                t.printStackTrace();
            }
        });
    }
}