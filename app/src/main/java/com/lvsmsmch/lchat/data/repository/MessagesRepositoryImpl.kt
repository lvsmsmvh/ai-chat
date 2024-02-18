package com.lvsmsmch.lchat.data.repository

import com.lvsmsmch.lchat.data.db.dao.MessagesDao
import com.lvsmsmch.lchat.domain.entities.ChatMessage
import com.lvsmsmch.lchat.domain.entities.NewMessage
import com.lvsmsmch.lchat.domain.entities.UpdateMessage
import com.lvsmsmch.lchat.domain.repositories.MessagesRepository
import com.lvsmsmch.lchat.utils.toChatMessage
import com.lvsmsmch.lchat.utils.toChatMessageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class MessagesRepositoryImpl(
    private val messagesDao: MessagesDao,
) : MessagesRepository {

    override fun getMessagesFlow(characterId: Int): Flow<List<ChatMessage>> {
        return messagesDao.getMessagesFlow(characterId).distinctUntilChanged().map { list ->
            list.map { it.toChatMessage() }
        }
    }

    override fun getMessages(characterId: Int): List<ChatMessage> {
        return messagesDao.getMessages(characterId).map { it.toChatMessage() }
    }

    override fun addMessageToHistory(characterId: Int, message: NewMessage) {
        val entity = message.toChatMessageEntity(characterId)
        messagesDao.insert(entity)
    }

    override fun updateMessage(characterId: Int, message: UpdateMessage) {
        val previous = messagesDao.getMessage(characterId, message.messageId)
        val new = previous.copy(
            messageText = message.messageText,
            isFailed = message.isFailed
        )
        messagesDao.insert(new)
    }

    override fun deleteMessages(characterId: Int, messageIds: List<Int>) {
        messageIds.forEach { messagesDao.deleteById(characterId, it) }
    }

}