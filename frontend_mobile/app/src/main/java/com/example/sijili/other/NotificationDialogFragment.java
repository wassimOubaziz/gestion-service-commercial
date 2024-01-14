package com.example.sijili.other;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sijili.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationDialogFragment extends DialogFragment {

    private List<String> notifications;
    private ArrayAdapter<String> adapter;

    public NotificationDialogFragment(List<String> notifications) {
        this.notifications = notifications;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_notification_dialog, null);

        ListView listView = view.findViewById(R.id.listViewNotifications);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notifications);
        listView.setAdapter(adapter);

        // Set a listener for item click to handle deletion
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Handle notification deletion here
            notifications.remove(position);
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}
