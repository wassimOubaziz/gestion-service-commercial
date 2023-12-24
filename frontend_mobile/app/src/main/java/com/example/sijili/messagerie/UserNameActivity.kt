package com.example.sijili.messagerie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.example.sijili.databinding.ActivityUserNameBinding
import com.example.sijili.messagerie.ChatActivity

class UserNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etUsername.doAfterTextChanged {
            val username = it.toString()
            binding.btnProceed.isEnabled = username.isNotEmpty()
        }

        binding.btnProceed.setOnClickListener {
            val userId = generateUserId()  // Replace this with your logic for generating userId
            val roomId = generateRoomId()  // Replace this with your logic for generating roomId
            val intent = Intent(this, ChatActivity::class.java)
//            intent.putExtra(ChatActivity.USER_ID, userId)
//            intent.putExtra(ChatActivity.ROOM_ID, roomId)
            startActivity(intent)
        }
    }

    private fun generateUserId(): String {
        // Implement your logic for generating userId here
        return "user_${System.currentTimeMillis()}"
    }

    private fun generateRoomId(): String {
        // Implement your logic for generating roomId here
        return "room_${System.currentTimeMillis()}"
    }

    override fun onResume() {
        super.onResume()
        binding.etUsername.requestFocus()
    }
}
