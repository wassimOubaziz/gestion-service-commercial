package com.example.sijili.messagerie

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sijili.databinding.ItemChatOtherBinding
import com.example.sijili.databinding.ItemChatSelfBinding

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_SELF = 1
    private val ITEM_OTHER = 2

    private val diffCallback = object : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitChat(chats: List<Chat>) {
        differ.submitList(chats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SELF) {
            val binding = ItemChatSelfBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SelfChatItemViewHolder(binding)
        } else {
            val binding = ItemChatOtherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            OtherChatItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SelfChatItemViewHolder -> holder.bind(differ.currentList[position])
            is OtherChatItemViewHolder -> holder.bind(differ.currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position].isSelf) ITEM_SELF else ITEM_OTHER
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class OtherChatItemViewHolder(val binding: ItemChatOtherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.apply {
                // Set layout parameters for the view, for example, align it to the left
                // You may need to adjust the layout parameters based on your layout design
                // to ensure messages are displayed on the correct side
                name.text = chat.userId
                msg.text = chat.message
            }
        }
    }

    inner class SelfChatItemViewHolder(val binding: ItemChatSelfBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.apply {
                // Set layout parameters for the view, for example, align it to the right
                // You may need to adjust the layout parameters based on your layout design
                // to ensure messages are displayed on the correct side
                name.text = "Client"
                msg.text = chat.message
            }
        }
    }
}
