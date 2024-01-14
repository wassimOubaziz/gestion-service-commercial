package com.example.sijili.messagerie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class SocketHandler(private val authToken: String) {

    private var socket: Socket? = null

    private val _onNewChat = MutableLiveData<Chat>()
    val onNewChat: LiveData<Chat> get() = _onNewChat

    private val _onOldMessages = MutableLiveData<List<Chat>>()
    val onOldMessages: LiveData<List<Chat>> get() = _onOldMessages

    private val _onSocketConnected = MutableLiveData<Boolean>()
    val onSocketConnected: LiveData<Boolean> get() = _onSocketConnected

    init {
        initializeSocket()
    }

    private fun initializeSocket() {
        try {
            val options = IO.Options()
            options.query = "token=$authToken"
            socket = IO.socket(SOCKET_URL, options)
            socket?.on(Socket.EVENT_CONNECT, Emitter.Listener {
                Log.d("SocketHandler", "Socket connected")
                _onSocketConnected.postValue(true)


            })?.on(Socket.EVENT_CONNECT_ERROR, Emitter.Listener { args ->
                Log.e("SocketHandler", "Socket connection error: ${args[0].toString()}")
                Log.e("SocketHandler", "url $SOCKET_URL")
                _onSocketConnected.postValue(false)
                // Handle connection error here if needed
            })

            socket?.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            Log.e("SocketHandler", "Error creating socket: ${e.message}")
            _onSocketConnected.postValue(false)
        }
    }

    fun registerOnNewChat() {
        socket?.on(CHAT_KEYS.BROADCAST, Emitter.Listener { args ->
            args?.let { d ->
                if (d.isNotEmpty()) {
                    val data = d[0]
                    Log.d("DATADEBUG", "$data")
                    if (data.toString().isNotEmpty()) {
                        val chat = Gson().fromJson(data.toString(), Chat::class.java)
                        _onNewChat.postValue(chat)
                    }
                }
            }
        })

        socket?.on(CHAT_KEYS.OLD_MESSAGES, Emitter.Listener { args ->
            args?.let { d ->
                if (d.isNotEmpty()) {
                    val messages = d.flatMap { json ->
                        try {
                            val jsonArray = JsonParser.parseString(json.toString()).asJsonArray
                            jsonArray.map { jsonObject ->
                                val userId = jsonObject.asJsonObject.get("userId").asString
                                val message = jsonObject.asJsonObject.get("message").asString
                                val roomId = jsonObject.asJsonObject.get("roomId").asString
                                val isSelf = jsonObject.asJsonObject.get("isSelf").asBoolean


                                // Create a new Chat instance with the updated isSelf property
                                Chat(userId = userId, roomId = roomId, message = message, isSelf = isSelf)
                            }
                        } catch (e: Exception) {
                            // Handle parsing error, if any
                            Log.e("SocketHandler", "Error parsing JSON: ${e.message}")
                            emptyList()
                        }
                    }
                    _onOldMessages.postValue(messages)
                }
            }
        })
    }


    fun connectSocket() {
        if (socket?.connected() != true) {
            initializeSocket()
        }
    }






    fun joinRoom(roomId: String) {
        if (socket?.connected() == true) {
            // Emit the join_room event with the provided roomId and authentication token
            socket?.emit("join_room", roomId)
        } else {
            Log.e("SocketHandler", "Socket is not connected. Cannot join room.")
            // Handle the case where the socket is not connected
        }
    }

    fun disconnectSocket() {
        socket?.disconnect()
        socket?.off()
        Log.d("SocketHandler", "Socket disconnected")
    }

    fun emitChat(chat: Chat) {
        if (socket?.connected() == true) {
            val jsonStr = Gson().toJson(chat, Chat::class.java)
            socket?.emit(CHAT_KEYS.NEW_MESSAGE, jsonStr)
        } else {
            Log.e("SocketHandler", "Socket is not connected. Message not sent.")
            // Handle the case where the socket is not connected
        }
    }

    private object CHAT_KEYS {
        const val NEW_MESSAGE = "new_message"
        const val BROADCAST = "broadcast"
        const val OLD_MESSAGES = "old_messages"
    }

    companion object {
        private const val SOCKET_URL = "http://192.168.43.59:4000/"
    }
}
