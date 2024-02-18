package com.lvsmsmch.lchat.presentation.pages.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvsmsmch.lchat.domain.entities.Character
import com.lvsmsmch.lchat.domain.entities.ChatMessage
import com.lvsmsmch.lchat.domain.entities.NewMessage
import com.lvsmsmch.lchat.domain.entities.Sender
import com.lvsmsmch.lchat.domain.entities.UpdateMessage
import com.lvsmsmch.lchat.domain.repositories.AiRepository
import com.lvsmsmch.lchat.domain.repositories.CharactersRepository
import com.lvsmsmch.lchat.domain.repositories.MessagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class ChatViewModel(
    private val characterId: Int,
    private val charactersRepository: CharactersRepository,
    private val aiRepository: AiRepository,
    private val messagesRepository: MessagesRepository,
) : ContainerHost<ChatState, ChatSideEffect>, ViewModel() {

    private lateinit var character: Character
    private var receivingMessageJob: Job? = null
    private var reloadingMessageJob: Job? = null

    private val selectedMessagesIds = mutableListOf<Int>()

    override val container = container<ChatState, ChatSideEffect>(
        ChatState()
    )

    init {
        intent {
            character = charactersRepository.getById(characterId).getOrThrow()

            reduce {
                state.copy(characterName = character.name, sendButtonEnabled = false)
            }

            messagesRepository.getMessagesFlow(character.id).collectLatest { messages ->
                reduce {
                    state.copy(
                        chatItems = messages.reversed().toChatItems(),
                        sendButtonEnabled = true
                    )
                }
            }
        }
    }

    /**
     * Public methods.
     */

    fun sendMessageFromUser(text: String) {
        intent {
            if (receivingMessageJob != null) {
                return@intent
            }
            receivingMessageJob = viewModelScope.launch(Dispatchers.IO) {

                messagesRepository.addMessageToHistory(
                    character.id, NewMessage(
                        sender = Sender.User,
                        messageText = text,
                    )
                )

                reduce {
                    state.copy(
                        chatItems = state.chatItems.removeItemWithId(ChatItem.NEXT_MESSAGE_ID),
                        sendButtonEnabled = false
                    )
                }

                val historyAll = messagesRepository.getMessages(characterId)
                val indexOfTargetMessage = 0
                aiRepository.getNextMessage(character, historyAll).collectLatest { streamingState ->
                    when (streamingState) {
                        is AiRepository.StreamingState.Idle -> {
                        }

                        is AiRepository.StreamingState.Loading -> {
                            reduce {
                                state.copy(
                                    chatItems = state.chatItems.insertChatItem(
                                        position = indexOfTargetMessage,
                                        item = ChatItem.CharacterLoading
                                    )
                                )
                            }
                        }

                        is AiRepository.StreamingState.Streaming -> {
                            reduce {
                                state.copy(
                                    chatItems = state.chatItems.insertChatItem(
                                        position = indexOfTargetMessage,
                                        item = ChatItem.CharacterTyping(streamingState.newMessage)
                                    )
                                )
                            }
                        }

                        is AiRepository.StreamingState.Finished -> {
                            messagesRepository.addMessageToHistory(
                                characterId = characterId,
                                message = NewMessage(
                                    sender = Sender.Character,
                                    messageText = streamingState.finalMessage,
                                    isFailed = false
                                )
                            )
                            receivingMessageJob = null
                        }

                        is AiRepository.StreamingState.Error -> {
                            messagesRepository.addMessageToHistory(
                                characterId = characterId,
                                message = NewMessage(
                                    sender = Sender.Character,
                                    isFailed = true
                                )
                            )
                            receivingMessageJob = null
                        }
                    }
                }
            }
        }
    }

    fun reloadFailedMessage(chatItem: ChatItem) {
        intent {
            if (reloadingMessageJob != null) {
                return@intent
            }
            reloadingMessageJob = viewModelScope.launch(Dispatchers.IO) {

                val targetMessageId = chatItem.id
                val indexOfTargetMessage = state.chatItems.indexOfFirst {
                    it.id == targetMessageId
                }

                reduce {
                    state.copy(
                        chatItems = state.chatItems.replaceChatItem(
                            position = indexOfTargetMessage,
                            item = ChatItem.CharacterLoading
                        )
                    )
                }

                val historyAll = messagesRepository.getMessages(characterId)

                val historyCut = historyAll.subList(
                    fromIndex = 0,
                    toIndex = historyAll.indexOfFirst { it.id == targetMessageId }
                )

                aiRepository.getNextMessage(character, historyCut).collectLatest { streamingState ->
                    when (streamingState) {
                        is AiRepository.StreamingState.Idle -> {
                        }

                        is AiRepository.StreamingState.Loading -> {
                        }

                        is AiRepository.StreamingState.Streaming -> {
                            reduce {
                                state.copy(
                                    chatItems = state.chatItems.insertChatItem(
                                        position = indexOfTargetMessage,
                                        item = ChatItem.CharacterTyping(streamingState.newMessage)
                                    )
                                )
                            }
                        }

                        is AiRepository.StreamingState.Finished -> {
                            messagesRepository.updateMessage(
                                characterId = characterId,
                                message = UpdateMessage(
                                    messageId = targetMessageId,
                                    messageText = streamingState.finalMessage,
                                    isFailed = false
                                )
                            )
                            refreshMessages()
                            reloadingMessageJob = null
                        }

                        is AiRepository.StreamingState.Error -> {
                            messagesRepository.updateMessage(
                                characterId = characterId,
                                message = UpdateMessage(
                                    messageId = targetMessageId,
                                    isFailed = true
                                )
                            )
                            refreshMessages()
                            reloadingMessageJob = null
                        }
                    }
                }
            }
        }
    }

    fun deleteMessages() {
        intent {
            messagesRepository.deleteMessages(characterId, selectedMessagesIds)
        }
    }

    fun selectMessage(chatItem: ChatItem) {
        intent {
            reduce {
                selectedMessagesIds.add(chatItem.id)
                val index = state.chatItems.indexOf(chatItem)
                val newList = state.chatItems.toMutableList()
                newList.removeAt(index)
                newList.add(index, chatItem.copyWithDifferentSelect(true))
                state.copy(
                    chatItems = newList,
                    isSelecting = selectedMessagesIds.isNotEmpty(),
                    selectedCount = selectedMessagesIds.size
                )
            }
        }
    }

    fun unselectMessage(chatItem: ChatItem) {
        intent {
            reduce {
                selectedMessagesIds.remove(chatItem.id)
                val index = state.chatItems.indexOf(chatItem)
                val newList = state.chatItems.toMutableList()
                newList.removeAt(index)
                newList.add(index, chatItem.copyWithDifferentSelect(false))
                state.copy(
                    chatItems = newList,
                    isSelecting = selectedMessagesIds.isNotEmpty(),
                    selectedCount = selectedMessagesIds.size
                )
            }
        }
    }

    fun unselectAllMessages() {
        intent {
            reduce {
                selectedMessagesIds.clear()
                val newList = state.chatItems.map {
                    it.copyWithDifferentSelect(isSelected = false)
                }
                state.copy(
                    chatItems = newList,
                    isSelecting = false,
                    selectedCount = 0
                )
            }
        }
    }

    fun selectAllMessages() {
        intent {
            reduce {
                selectedMessagesIds.clear()
                val newList = state.chatItems.map {
                    selectedMessagesIds.add(it.id)
                    it.copyWithDifferentSelect(isSelected = true)
                }
                state.copy(
                    chatItems = newList,
                    isSelecting = true,
                    selectedCount = selectedMessagesIds.size
                )
            }
        }
    }


    /**
     * Helper methods/extensions.
     */

    private fun refreshMessages() {
        intent {
            reduce {
                state.copy(
                    chatItems = messagesRepository.getMessages(characterId)
                        .reversed().toChatItems(),
                    sendButtonEnabled = true
                )
            }
        }
    }

    private fun List<ChatMessage>.toChatItems(): List<ChatItem> {
        return map { it.toChatItem() }
    }

    private fun List<ChatItem>.removeItemWithId(targetId: Int): List<ChatItem> {
        val newList = this.toMutableList()
        newList.removeIf { it.id == targetId }
        return newList
    }

    private fun List<ChatItem>.insertChatItem(position: Int, item: ChatItem): List<ChatItem> {
        val newList = this.toMutableList()
        newList.removeIf { it.id == ChatItem.NEXT_MESSAGE_ID }
        newList.add(position, item)
        return newList
    }

    private fun List<ChatItem>.replaceChatItem(position: Int, item: ChatItem): List<ChatItem> {
        val newList = this.toMutableList()
        newList[position] = item
        return newList
    }

    private fun ChatMessage.toChatItem(): ChatItem {
        return when {
            this.sender == Sender.User -> {
                ChatItem.UserMessage(
                    id = id,
                    messageText = messageText,
                    sendTimeMillis = sendTimeMillis,
                )
            }

            isFailed -> {
                ChatItem.CharacterFailed(
                    id = id,
                    sendTimeMillis = sendTimeMillis,
                )
            }

            else -> {
                ChatItem.CharacterMessage(
                    id = id,
                    messageText = messageText,
                    sendTimeMillis = sendTimeMillis,
                )
            }
        }
    }
}