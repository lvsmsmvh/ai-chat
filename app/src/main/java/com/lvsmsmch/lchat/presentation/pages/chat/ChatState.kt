package com.lvsmsmch.lchat.presentation.pages.chat

data class ChatState(
    val characterName: String = "",
    val chatItems: List<ChatItem> = emptyList(),
    val sendButtonEnabled: Boolean = false,
    val isSelecting: Boolean = false,
    val selectedCount: Int = 0,
)