package com.lvsmsmch.lchat.domain.repositories

import com.lvsmsmch.lchat.domain.entities.Character
import com.lvsmsmch.lchat.domain.entities.ChatMessage
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface AiRepository {

    sealed class StreamingState {
        data object Idle: StreamingState()
        data object Loading: StreamingState()
        data class Streaming(val newMessage: String): StreamingState()
        data class Finished(val finalMessage: String): StreamingState()
        data class Error(val exception: Exception): StreamingState()
    }

    suspend fun getNextMessage(
        character: Character,
        history: List<ChatMessage>
    ): Flow<StreamingState>
}