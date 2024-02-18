package com.lvsmsmch.lchat.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lvsmsmch.lchat.domain.entities.Sender


@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val characterId: Int,
    val sender: Sender,
    val messageText: String,
    val sendTimeMillis: Long,
    val isFailed: Boolean,
)