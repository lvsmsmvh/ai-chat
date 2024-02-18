package com.lvsmsmch.lchat.domain.entities

data class NewMessage(
    val sender: Sender,
    val messageText: String = "",
    val isFailed: Boolean = false,
)