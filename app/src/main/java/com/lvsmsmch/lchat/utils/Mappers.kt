package com.lvsmsmch.lchat.utils

import com.lvsmsmch.lchat.data.db.entities.ChatMessageEntity
import com.lvsmsmch.lchat.domain.entities.ChatMessage
import com.lvsmsmch.lchat.domain.entities.NewMessage

fun ChatMessageEntity.toChatMessage() = ChatMessage(
    id = id,
    characterId = characterId,
    sender = sender,
    messageText = messageText,
    sendTimeMillis = sendTimeMillis,
    isFailed = isFailed,
    isTyping = false
)

fun NewMessage.toChatMessageEntity(characterId: Int) = ChatMessageEntity(
    characterId = characterId,
    sender = sender,
    messageText = messageText,
    sendTimeMillis = System.currentTimeMillis(),
    isFailed = isFailed
)