package com.example.sijili.messagerie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class Chat(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val userId: String,
    val roomId: String,
    val message: String,
    var isSelf: Boolean = false
)
