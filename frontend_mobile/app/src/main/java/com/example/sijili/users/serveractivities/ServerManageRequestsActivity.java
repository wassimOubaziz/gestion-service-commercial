package com.example.sijili.users.serveractivities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sijili.BaseActivity;
import com.example.sijili.R;
import com.example.sijili.RetrofitInterface;
import com.example.sijili.adapters.CommerceRequestAdapter;
import com.example.sijili.other.NavigationUtil;
import com.example.sijili.requests.CommerceRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerManageRequestsActivity extends BaseActivity {
    private static final String BASE_URL = "http://192.168.43.59:4000"; // Replace with your server URL

    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_manage_requests);
        setupNavigationDrawer();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Fetch commerce requests
        fetchCommerceRequests();
    }

    private void fetchCommerceRequests() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String authToken = "Bearer " + preferences.getString("token", "");
        showLoadingDialog();
        Call<List<CommerceRequest>> call = retrofitInterface.getCommerceRequests(authToken);

        call.enqueue(new Callback<List<CommerceRequest>>() {
            @Override
            public void onResponse(Call<List<CommerceRequest>> call, Response<List<CommerceRequest>> response) {
                dismissLoadingDialog();
                if (response.isSuccessful() && response.body() != null) {
                    displayCommerceRequests(response.body());
                    Toast.makeText(ServerManageRequestsActivity.this, "fetch 2 success", Toast.LENGTH_LONG).show();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(ServerManageRequestsActivity.this, "fetch 3 failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CommerceRequest>> call, Throwable t) {
                dismissLoadingDialog();
                // Handle network errors or other failures
                Toast.makeText(ServerManageRequestsActivity.this, "fetch 4 failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayCommerceRequests(List<CommerceRequest> commerceRequests) {
        CommerceRequestAdapter adapter = new CommerceRequestAdapter(commerceRequests, retrofitInterface);
        recyclerView.setAdapter(adapter);
    }
    public void onUserButtonClick(View view) {
        NavigationUtil.navigateToProfile(this);
    }

}
