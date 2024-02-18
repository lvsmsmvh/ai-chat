package com.lvsmsmch.lchat.domain.repositories

import com.lvsmsmch.lchat.domain.entities.ChatMessage
import com.lvsmsmch.lchat.domain.entities.NewMessage
import com.lvsmsmch.lchat.domain.entities.UpdateMessage
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    fun getMessagesFlow(characterId: Int): Flow<List<ChatMessage>>
    fun getMessages(characterId: Int): List<ChatMessage>
    fun addMessageToHistory(characterId: Int, message: NewMessage)
    fun updateMessage(characterId: Int, message: UpdateMessage)
    fun deleteMessages(characterId: Int, messageIds: List<Int>)
}