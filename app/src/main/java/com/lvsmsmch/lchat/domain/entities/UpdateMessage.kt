package com.lvsmsmch.lchat.domain.entities

data class UpdateMessage(
    val messageId: Int,
    val messageText: String = "",
    val isFailed: Boolean = false,
)