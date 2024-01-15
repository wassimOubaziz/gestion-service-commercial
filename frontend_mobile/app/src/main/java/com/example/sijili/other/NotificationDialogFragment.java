package com.example.sijili.other;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sijili.R;
import com.example.sijili.RetrofitInterface;
import com.example.sijili.requests.CommerceRequest;
import com.example.sijili.users.serveractivities.ServerRequestInfos;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.appcompat.app.AlertDialog;

public class NotificationDialogFragment extends DialogFragment {

    private List<String> notifications;
    private List<String> notificationsTimestamp;
    private ArrayAdapter<String> adapter;
    public AlertDialog loadingDialog;

    private static final String BASE_URL = "http://192.168.43.59:4000"; // Replace with your server URL

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public NotificationDialogFragment(List<String> notifications, List<String> notificationsTimestamp) {
        this.notifications = notifications;
        this.notificationsTimestamp = notificationsTimestamp;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        return new AlertDialog.Builder(getActivity())
                .setTitle("Notifications")
                .setView(createDialogView())
                .setPositiveButton("Close", (dialog, which) -> dismiss())
                .create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_dialog, container, false);
    }

    private View createDialogView() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String authToken = "Bearer " + preferences.getString("token", "");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_notification_dialog, null);

        ListView listView = view.findViewById(R.id.listViewNotifications);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notifications);
        listView.setAdapter(adapter);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


        // Set a listener for item click to handle deletion
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Handle notification deletion here
            String selectedNotificationTimestamp = notificationsTimestamp.get(position);
            showLoadingDialog();
            Call<Void> call = retrofitInterface.deleteNotification(authToken, selectedNotificationTimestamp);
            call.enqueue(new Callback<Void>() {

                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        // Notification deleted successfully from the backend

                    } else {
                        // Handle error
                        Log.e("DeleteNotification", "Error deleting notification");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    //dismissLoadingDialog();
                    // Handle failure
                    Log.e("DeleteNotification", "Failed to delete notification", t);

                }
            });
            notifications.remove(position);
            adapter.notifyDataSetChanged();
        });

        return view;
    }
    protected void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.activity_loading, null);
        builder.setView(view);
        builder.setCancelable(false);
        loadingDialog = builder.create();
        loadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

}
