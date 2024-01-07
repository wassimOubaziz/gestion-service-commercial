package com.example.sijili.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sijili.R;
import com.example.sijili.messagerie.ChatActivity;
import com.example.sijili.requests.MessageRequest;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> implements Filterable {

    private List<MessageRequest> messageRequests;
    private List<MessageRequest> filteredList; // Add this list
    private MessageFilter messageFilter;
    private Context context; // Add this variable

    public MessageAdapter(List<MessageRequest> messageRequests, Context context) {
        this.messageRequests = messageRequests;
        this.context = context; // Initialize the context
        this.filteredList = new ArrayList<>(messageRequests); // Initialize filtered list
        this.messageFilter = new MessageFilter();
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

        holder.relativeLayoutConversation.setOnClickListener(new View.OnClickListener() {
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

    public void setData(List<MessageRequest> newData) {
        messageRequests = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return messageFilter;
    }

    private class MessageFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<MessageRequest> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(messageRequests);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (MessageRequest message : messageRequests) {
                    if (message.getLast_name().toLowerCase().contains(filterPattern)
                            || message.getEmail().toLowerCase().contains(filterPattern)
                            || message.getMessage().toLowerCase().contains(filterPattern)
                            || message.getTimestamp().toLowerCase().contains(filterPattern)
                            || message.getUserId().toLowerCase().contains(filterPattern)) {
                        filteredList.add(message);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Clear the original list
            messageRequests.clear();

            // Add the filtered data to the original list
            messageRequests.addAll((List<MessageRequest>) results.values);

            // Notify the adapter that the data has changed
            notifyDataSetChanged();
        }

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView2;
        TextView textViewUserName;
        TextView textViewUserEmail;
        TextView textViewMessage;
        TextView textViewTimestamp;
        TextView tvUserid;
        RelativeLayout relativeLayoutConversation;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView2 = itemView.findViewById(R.id.imageView2);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserEmail = itemView.findViewById(R.id.textViewUserEmail);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            tvUserid = itemView.findViewById(R.id.tvUserid);
            relativeLayoutConversation = itemView.findViewById(R.id.relativeMessage);
        }
    }
}
