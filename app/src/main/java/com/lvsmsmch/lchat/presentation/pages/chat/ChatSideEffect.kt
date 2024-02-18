package com.lvsmsmch.lchat.presentation.pages.chat

sealed class ChatSideEffect {
    data object Back : ChatSideEffect()
}