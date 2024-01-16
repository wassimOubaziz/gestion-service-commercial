package com.example.sijili.users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sijili.BaseActivity;
import com.example.sijili.BusinessRequestCount;
import com.example.sijili.R;
import com.example.sijili.RetrofitInterface;
import com.example.sijili.messagerie.SocketHandler;
import com.example.sijili.other.NavigationUtil;
import com.example.sijili.users.serveractivities.ServerManageRequestsActivity;
import com.example.sijili.users.serveractivities.ServerMessagesActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerHomeActivity extends BaseActivity {

    private TextView welcomeText;
    private BarChart barChart;


    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.168.43.59:4000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);
        displayClientName();
        setupNavigationDrawer();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Initialize BarChart
        barChart = findViewById(R.id.barChart);
        fetchBusinessRequestsCount();
    }

    private void displayClientName() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String name = preferences.getString("name", "");
        welcomeText = findViewById(R.id.welcomeClientText);
        welcomeText.setText(welcomeText.getText().toString() + " " + name);
    }

    private void fetchBusinessRequestsCount() {
        Call<List<BusinessRequestCount>> call = retrofitInterface.getBusinessRequestsCount();
        call.enqueue(new Callback<List<BusinessRequestCount>>() {
            @Override
            public void onResponse(Call<List<BusinessRequestCount>> call, Response<List<BusinessRequestCount>> response) {
                if (response.isSuccessful()) {
                    List<BusinessRequestCount> businessRequestCounts = response.body();
                    // Process the businessRequestCounts and update the BarChart
                    updateBarChart(businessRequestCounts);
                } else {
                    // Handle error
                    Toast.makeText(ServerHomeActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BusinessRequestCount>> call, Throwable t) {
                // Handle failure
                Toast.makeText(ServerHomeActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBarChart(List<BusinessRequestCount> businessRequestCounts) {
        // Process the businessRequestCounts and update the BarChart
        // Example: Convert businessRequestCounts to BarEntry list and update the BarChart
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (BusinessRequestCount count : businessRequestCounts) {
            entries.add(new BarEntry(count.getMonth(), count.getCount()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Business Requests");
        BarData barData = new BarData(dataSet);

        barChart.setData(barData);

        // Customize your BarChart here
        // For example, set a description
        Description description = new Description();
        description.setText("Business Requests in the Last 12 Months");
        barChart.setDescription(description);

        // Customize x-axis labels
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(months));
        barChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
    }



    // Other methods in your activity

    public void goToManageRequest(View view) {
        Intent intent = new Intent(ServerHomeActivity.this, ServerManageRequestsActivity.class);
        startActivity(intent);
    }

    public void goToMessagesList(View view) {
        Intent intent = new Intent(ServerHomeActivity.this, ServerMessagesActivity.class);
        startActivity(intent);
    }

    public void onUserButtonClick(View view) {
        NavigationUtil.navigateToProfile(this);
    }
}