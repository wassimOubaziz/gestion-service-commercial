package com.example.sijili.users.serveractivities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sijili.R;
import com.example.sijili.RetrofitInterface;
import com.example.sijili.adapters.CommerceRequestAdapter;
import com.example.sijili.adapters.MessageAdapter;
import com.example.sijili.requests.CommerceRequest;
import com.example.sijili.requests.MessageRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerMessagesActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://192.168.1.41:4000"; // Replace with your server URL
    private RecyclerView recyclerViewMessagesList;
    private RetrofitInterface retrofitInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_messages);

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
    }

    private void fetchMessages() {
        // Assuming you have a Retrofit instance named "retrofit" initialized elsewhere
        Call<List<MessageRequest>> call = retrofitInterface.getMessages();
        call.enqueue(new Callback<List<MessageRequest>>() {
            @Override
            public void onResponse(Call<List<MessageRequest>> call, Response<List<MessageRequest>> response) {
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
                // Handle failure
                Toast.makeText(ServerMessagesActivity.this, "fetch 4 failed", Toast.LENGTH_LONG).show();

            }
        });
    }
    private void displayCommerceMessages(List<MessageRequest> messageRequests) {
        MessageAdapter adapter = new MessageAdapter(messageRequests, ServerMessagesActivity.this);
        recyclerViewMessagesList.setAdapter(adapter);
    }
}
