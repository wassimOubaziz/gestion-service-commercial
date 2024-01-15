package com.example.sijili.adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sijili.R;
import com.example.sijili.requests.BusinessRequest;
import com.example.sijili.users.clientactivities.ClientFollowRequestInfos;

import java.util.List;

public class MyBusinessRequestAdapter extends RecyclerView.Adapter<MyBusinessRequestAdapter.ViewHolder> {
    private List<BusinessRequest> businessRequests;
    private String authToken = "";

    public MyBusinessRequestAdapter(List<BusinessRequest> businessRequests) {
        this.businessRequests = businessRequests;
    }

    public MyBusinessRequestAdapter(String authToken) {
        this.authToken = authToken;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_business_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusinessRequest businessRequest = businessRequests.get(position);


        // Bind your data to the views here
        holder.textViewActivityType.setText(businessRequest.getActivityType());
        holder.textViewCompanyName.setText(businessRequest.getCompanyName());
        holder.textViewStatus.setText(String.valueOf(businessRequest.getStatus()));
//        holder.imageViewPay.setImageResource(R.drawable.paid_icon);
        if (businessRequest.getStatus().equals("completed")) {
            holder.textViewStatus.setText(R.string.status_completed);
            holder.textViewStatus.setTextColor(Color.parseColor("#006400"));
        } else if (businessRequest.getStatus().equals("refuse")) {
            holder.textViewStatus.setText(R.string.status_refused);
            holder.textViewStatus.setTextColor(Color.parseColor("#8B0000"));
        } else if (businessRequest.getStatus().equals("in progression")) {
            holder.textViewStatus.setText(R.string.status_pending);
            holder.textViewStatus.setTextColor(Color.parseColor("#FF8C00"));
        } else {
            // Reset text color to the default color if it's not "completed"
//            holder.textViewStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.primary_text_light));
            // You can set the desired default color here
        }
        holder.textViewStatus.setTypeface(null, Typeface.BOLD);
        if (businessRequest.isPaid()) {
            holder.imageViewPay.setImageResource(R.drawable.paid_icon);
        } else {
            holder.imageViewPay.setImageResource(R.drawable.not_paid_icon);
        }
        holder.tableRowClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, ClientFollowRequestInfos.class);
                intent.putExtra("requestId", businessRequest.getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return businessRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewActivityType;
        TextView textViewCompanyName;
        TextView textViewStatus;
        //        TextView textViewPay;
        ImageView imageViewPay;
        TableRow tableRowClicked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewActivityType = itemView.findViewById(R.id.textViewActivityType);
            textViewCompanyName = itemView.findViewById(R.id.textViewCompanyName);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            imageViewPay = itemView.findViewById(R.id.imageViewPay);
            tableRowClicked = itemView.findViewById(R.id.tableRowClicked);
        }
    }
}