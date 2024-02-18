package com.lvsmsmch.lchat.domain.entities

data class ChatMessage(
    val id: Int,
    val characterId: Int,
    val sender: Sender,
    val messageText: String,
    val sendTimeMillis: Long,
    val isFailed: Boolean,
    val isTyping: Boolean,
)