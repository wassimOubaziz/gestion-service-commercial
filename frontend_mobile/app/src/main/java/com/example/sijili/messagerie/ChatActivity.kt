package com.example.sijili.messagerie

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sijili.databinding.ActivityMain2Binding

class ChatActivity : AppCompatActivity() {

    private lateinit var socketHandler: SocketHandler
    private lateinit var binding: ActivityMain2Binding
    private lateinit var chatAdapter: ChatAdapter

    private val chatList = mutableListOf<Chat>()

    private var authToken = ""
    private var roomId = "" // Add roomId variable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the token from SharedPreferences
        authToken = retrieveAuthToken()

        roomId = intent.getStringExtra(ROOM_ID) ?: ""

        // Pass the token to SocketHandler
        socketHandler = SocketHandler(authToken)

        // Observe old messages
        socketHandler.onOldMessages.observe(this, Observer { oldMessages ->
            val modifiedOldMessages = oldMessages.map { it.copy(isSelf = it.isSelf) }
            chatList.addAll(modifiedOldMessages)
            chatAdapter.submitChat(chatList)
            binding.rvChat.scrollToPosition(chatList.size - 1)
        })

        // Join the room when the activity starts (without explicitly passing roomId)
        socketHandler.joinRoom(roomId)

        chatAdapter = ChatAdapter()

        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }

        // Observe socket connection status
        socketHandler.onSocketConnected.observe(this) { isConnected ->
            if (isConnected) {
                // Now that the socket is connected, join the room
                socketHandler.joinRoom(roomId)
            }
        }

        binding.btnSend.setOnClickListener {
            val message = binding.etMsg.text.toString()
            if (message.isNotEmpty()) {
                val isSelf = true // You are always the sender
                val chat = Chat(
                    userId = "", // No need to specify, as it's not used on the server
                    roomId = roomId, // No need to specify, as it's not used on the server
                    message = message,
                    isSelf = isSelf
                )
                socketHandler.emitChat(chat)
                binding.etMsg.setText("")
            }
        }

        socketHandler.onNewChat.observe(this) {
            val chat = it.copy(isSelf = roomId.isEmpty())
            chatList.add(chat)
            chatAdapter.submitChat(chatList)
            binding.rvChat.scrollToPosition(chatList.size - 1)
        }
    }

    override fun onDestroy() {
        socketHandler.disconnectSocket()
        super.onDestroy()
    }

    private fun retrieveAuthToken(): String {
        val preferences: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        return preferences.getString("token", "") ?: ""
    }

    companion object {
        const val ROOM_ID = "roomId"
    }
}
