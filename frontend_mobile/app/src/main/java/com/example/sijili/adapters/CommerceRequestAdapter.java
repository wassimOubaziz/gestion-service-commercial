package com.example.sijili.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sijili.R;
import com.example.sijili.RetrofitInterface;
import com.example.sijili.other.DialogUtils;
import com.example.sijili.requests.BusinessRequest;
import com.example.sijili.requests.CommerceRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sijili.RetrofitInterface;
import com.example.sijili.R;
import com.example.sijili.requests.CommerceRequest;
import com.example.sijili.users.serveractivities.ServerManageRequestsActivity;
import com.example.sijili.users.serveractivities.ServerRequestInfos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommerceRequestAdapter extends RecyclerView.Adapter<CommerceRequestAdapter.ViewHolder> {

    private List<CommerceRequest> commerceRequests;
    private static final String BASE_URL = "http://192.168.43.59:4000"; // Replace with your server URL

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String authToken = "";
    private AlertDialog loadingDialog;



    public CommerceRequestAdapter(List<CommerceRequest> commerceRequests) {
        this.commerceRequests = commerceRequests;
    }
    public CommerceRequestAdapter(List<CommerceRequest> commerceRequests, RetrofitInterface retrofitInterface) {
        this.commerceRequests = commerceRequests;
        this.retrofitInterface = retrofitInterface;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commerce_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CommerceRequest request = commerceRequests.get(position);

        holder.nameTextView.setText("Name: " + request.getName());
        holder.activityTypeTextView.setText("Activity Type: " + request.getActivityType());
        holder.companyTextView.setText("Company: " + request.getCompanyName());
        if ((request.getStatus() != null && request.getStatus().equalsIgnoreCase("completed"))||(request.isPaid() == false) ) {
            holder.relativeComponentRequest.setVisibility(View.GONE);
            holder.relativeComponentRequest.setLayoutParams(new RecyclerView.LayoutParams(0, 0)); // Set width and height to 0
            holder.rqInfosLayout.setOnClickListener(null); // Remove click listener
        }else {

            holder.rqInfosLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = holder.itemView.getContext();
                    Intent intent = new Intent(context, ServerRequestInfos.class);
                    intent.putExtra("requestId2", request.getId());
                    context.startActivity(intent);
                }
            });

            // Set click listeners for accept and refuse buttons
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("You are going to accept this request.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    showLoadingDialog(v.getContext());
                                    // User clicked Yes button
                                    if (retrofitInterface != null) {
                                        acceptCommerceRequest(request.getId(), v.getContext(), position);
                                    } else {
                                        Toast.makeText(v.getContext(), "RetrofitInterface is null", Toast.LENGTH_SHORT).show();
                                    }
                                    dismissLoadingDialog();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked No button, do nothing
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            holder.refuseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure you want to refuse this request?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked Yes button
                                    showLoadingDialog(v.getContext());
                                    if (retrofitInterface != null) {
                                        deleteCommerceRequest(request.getId(), v.getContext(), position);
                                    } else {
                                        Toast.makeText(v.getContext(), "RetrofitInterface is null", Toast.LENGTH_SHORT).show();
                                    }
                                    dismissLoadingDialog();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked No button, do nothing
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return commerceRequests.size();
    }
    private void acceptCommerceRequest(String requestId, Context context, int position) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String authToken = "Bearer " + preferences.getString("token", "");

        Call<CommerceRequest> call = retrofitInterface.updateBusinessRequestStatus(authToken, requestId);

        call.enqueue(new Callback<CommerceRequest>() {
            @Override
            public void onResponse(Call<CommerceRequest> call, Response<CommerceRequest> response) {
                if (response.isSuccessful()) {
                    // Handle successful update
                    CommerceRequest updatedRequest = response.body();

                    // You can update your local data or UI as needed
                    Toast.makeText(context, "Request accepted successfully", Toast.LENGTH_SHORT).show();
                    DialogUtils.showWarningDialog(context, context.getString(R.string.accept_request_dialogue));
                    removeItem(position);

                } else {
                    // Handle unsuccessful update
                    Toast.makeText(context, "Failed to accept requestx", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommerceRequest> call, Throwable t) {
                // Handle network errors or other failures
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteCommerceRequest(String requestId, Context context, int position) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String authToken = "Bearer " + preferences.getString("token", "");

        Call<Void> call = retrofitInterface.deleteCommerceRequest(authToken, requestId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {


                if (response.isSuccessful()) {
                    // Handle successful deletion, if needed
                    Toast.makeText(context, "Refused succefully", Toast.LENGTH_SHORT).show();
                    DialogUtils.showWarningDialog(context, context.getString(R.string.refuse_request_dialogue));
                    removeItem(position);

                } else {
                    // Handle unsuccessful deletion
                    Toast.makeText(context, "Failed to refuse", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network errors or other failures
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void removeItem(int position) {
        commerceRequests.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    private void showLoadingDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_loading, null);
        builder.setView(view);
        builder.setCancelable(false);
        loadingDialog = builder.create();
        loadingDialog.show();
    }

    private void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView activityTypeTextView;
        TextView companyTextView;
        ImageButton acceptButton;
        ImageButton refuseButton;

        LinearLayout rqInfosLayout;
        RelativeLayout relativeComponentRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.requestName);
            activityTypeTextView = itemView.findViewById(R.id.requestActivityType);
            companyTextView = itemView.findViewById(R.id.requestCompanyName);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            refuseButton = itemView.findViewById(R.id.refuseButton);
            rqInfosLayout = itemView.findViewById(R.id.layout_infos_rq);
            relativeComponentRequest = itemView.findViewById(R.id.relativeComponentRequest);
        }
    }
}