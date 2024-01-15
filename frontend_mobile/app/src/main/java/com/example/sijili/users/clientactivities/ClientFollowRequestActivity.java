package com.example.sijili.users.clientactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sijili.BaseActivity;
import com.example.sijili.ForgotPasswordActivity;
import com.example.sijili.LoginActivity;
import com.example.sijili.R;
import com.example.sijili.RetrofitInterface;
import com.example.sijili.adapters.MyBusinessRequestAdapter;
import com.example.sijili.other.NavigationUtil;
import com.example.sijili.requests.BusinessRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientFollowRequestActivity extends BaseActivity {
    private static final String BASE_URL = "http://192.168.43.59:4000"; // Replace with your server URL
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private RecyclerView recyclerView;
    private MyBusinessRequestAdapter adapter;
    private List<BusinessRequest> businessRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_follow_request);
        setupNavigationDrawer();

        recyclerView = findViewById(R.id.recyclerViewMyRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        businessRequests = new ArrayList<>();
        adapter = new MyBusinessRequestAdapter(businessRequests);

        recyclerView.setAdapter(adapter);

        // Create a Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        fetchMyCommerceRequests();
    }

    private void fetchMyCommerceRequests() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String authToken = preferences.getString("token", "");
        String authTokenHeader = "Bearer " + authToken;
        showLoadingDialog();
        Call<List<BusinessRequest>> call = retrofitInterface.getMyBusinessRequests(authTokenHeader);
        call.enqueue(new Callback<List<BusinessRequest>>() {
            @Override
            public void onResponse(Call<List<BusinessRequest>> call, Response<List<BusinessRequest>> response) {
                dismissLoadingDialog();
                if (response.isSuccessful() && response.body() != null) {
                    businessRequests.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ClientFollowRequestActivity.this, "this is it", Toast.LENGTH_LONG).show();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(ClientFollowRequestActivity.this, "fAILED", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<BusinessRequest>> call, Throwable t) {
                dismissLoadingDialog();
                // Handle failure
                Toast.makeText(ClientFollowRequestActivity.this, "Network error", Toast.LENGTH_LONG).show();

            }
        });
    }

    /*public void goToRequestInfos(View view) {
        Intent intent = new Intent(ClientFollowRequestActivity.this, ClientFollowRequestInfos.class);
        startActivity(intent);
    }
*/

    //toGoToUserInfos
    public void onUserButtonClick(View view) {
        NavigationUtil.navigateToProfile(this);
    }

}