package com.example.sijili.users.serveractivities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sijili.BaseActivity;
import com.example.sijili.R;
import com.example.sijili.RetrofitInterface;
import com.example.sijili.adapters.CommerceRequestAdapter;
import com.example.sijili.adapters.MessageAdapter;
import com.example.sijili.other.NavigationUtil;
import com.example.sijili.requests.CommerceRequest;
import com.example.sijili.requests.MessageRequest;
import com.google.android.material.search.SearchBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerMessagesActivity extends BaseActivity {
    private static final String BASE_URL = "http://192.168.43.59:4000"; // Replace with your server URL
    private RecyclerView recyclerViewMessagesList;
    private RetrofitInterface retrofitInterface;
    private MessageAdapter adapter;

    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_messages);
        setupNavigationDrawer();

        recyclerViewMessagesList = findViewById(R.id.recyclerViewMessagesList);
        recyclerViewMessagesList.setLayoutManager(new LinearLayoutManager(this));

        // Create a Retrofit instance
        retrofitInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitInterface.class);

        // Fetch commerce requests
        fetchMessages();

        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (recyclerViewMessagesList.getAdapter() instanceof MessageAdapter) {
                    ((MessageAdapter) recyclerViewMessagesList.getAdapter()).getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private void fetchMessages() {
        showLoadingDialog();
        Call<List<MessageRequest>> call = retrofitInterface.getMessages();
        call.enqueue(new Callback<List<MessageRequest>>() {
            @Override
            public void onResponse(Call<List<MessageRequest>> call, Response<List<MessageRequest>> response) {
                dismissLoadingDialog();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ServerMessagesActivity.this, "fetch 2 success" + response.body(), Toast.LENGTH_LONG).show();
                    displayCommerceMessages(response.body());
                } else {
                    // Handle error
                    Toast.makeText(ServerMessagesActivity.this, "fetch 3 failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<MessageRequest>> call, Throwable t) {
                dismissLoadingDialog();
                // Handle failure
                Toast.makeText(ServerMessagesActivity.this, "Network error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayCommerceMessages(List<MessageRequest> messageRequests) {
        adapter = new MessageAdapter(messageRequests, ServerMessagesActivity.this);
        recyclerViewMessagesList.setAdapter(adapter);
    }


    public void onUserButtonClick(View view) {
        NavigationUtil.navigateToProfile(this);
    }
}
