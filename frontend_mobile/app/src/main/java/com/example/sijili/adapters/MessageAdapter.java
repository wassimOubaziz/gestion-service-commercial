package com.example.sijili.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sijili.R;  // Change this to your actual package name
import com.example.sijili.RetrofitInterface;
import com.example.sijili.messagerie.ChatActivity;
import com.example.sijili.requests.CommerceRequest;
import com.example.sijili.requests.MessageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageRequest> messageRequests;
    private RetrofitInterface retrofitInterface;
    private Context context; // Add this variable

    public MessageAdapter(List<MessageRequest> messageRequests, Context context) {
        this.messageRequests = messageRequests;
        this.context = context; // Initialize the context
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageRequest message = messageRequests.get(position);

        holder.textViewUserName.setText(message.getLast_name());
        holder.textViewUserEmail.setText(message.getEmail());
        holder.textViewMessage.setText(message.getMessage());
        holder.textViewTimestamp.setText(message.getTimestamp());
        holder.tvUserid.setText(message.getUserId());

        holder.constraintLayoutConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class); // Use context
                intent.putExtra("roomId", message.getUserId());
                context.startActivity(intent); // Use context to start the activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageRequests.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView2;
        TextView textViewUserName;
        TextView textViewUserEmail;
        TextView textViewMessage;
        TextView textViewTimestamp;
        TextView tvUserid;
        ConstraintLayout constraintLayoutConversation;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView2 = itemView.findViewById(R.id.imageView2);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserEmail = itemView.findViewById(R.id.textViewUserEmail);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            tvUserid = itemView.findViewById(R.id.tvUserid);
            constraintLayoutConversation = itemView.findViewById(R.id.message_conversation);
        }
    }
}
