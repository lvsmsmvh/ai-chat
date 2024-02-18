package com.lvsmsmch.lchat.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.lvsmsmch.lchat.data.server.RetrofitBuilder
import com.lvsmsmch.lchat.data.server.open_ai.OpenAiApiService
import com.lvsmsmch.lchat.data.server.open_ai.OpenAiChunk
import com.lvsmsmch.lchat.data.server.open_ai.OpenAiMessage
import com.lvsmsmch.lchat.data.server.open_ai.OpenAiRequestStream
import com.lvsmsmch.lchat.domain.entities.Character
import com.lvsmsmch.lchat.domain.entities.ChatMessage
import com.lvsmsmch.lchat.domain.entities.Sender
import com.lvsmsmch.lchat.domain.repositories.AiRepository
import com.lvsmsmch.lchat.utils.RemoteConfigHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class AiRepositoryOpenAiImpl(
    private val context: Context
) : AiRepository {

    companion object {
        private const val DELAY_BETWEEN_CHUNKS_MS = 100L
        private const val DELAY_BEFORE_START_LOADING = 250L
    }

    private val openAiService by lazy {
        RetrofitBuilder(
            OpenAiApiService::class.java,
            "https://api.openai.com/"
        ).build()
    }


    override suspend fun getNextMessage(
        character: Character,
        history: List<ChatMessage>
    ): Flow<AiRepository.StreamingState> {
        val streamingFlow = MutableStateFlow<AiRepository.StreamingState>(
            value = AiRepository.StreamingState.Idle
        )

        CoroutineScope(Dispatchers.IO).launch {
            delay(DELAY_BEFORE_START_LOADING)

            ensureActive()

            if (streamingFlow.value == AiRepository.StreamingState.Idle) {
                streamingFlow.emit(AiRepository.StreamingState.Loading)
            }

            val request = OpenAiRequestStream(
                model = "gpt-3.5-turbo-1106",
                messages = prepareListOfMessages(character, history),
                temperature = 0.7,
                stream = true
            )

            val call = openAiService.getChatCompletionsStream(
                contentType = "application/json",
                authorization = "Bearer ${RemoteConfigHelper.openAiKey()}",
                request = request
            )

            val response = try {
                call.execute()
            } catch (e: IOException) {
                Log.d("ai_call", "Error ${e.message}")
                streamingFlow.emit(AiRepository.StreamingState.Error(e))
                return@launch
            }

            ensureActive()

            val responseBody = response.body()
            if (responseBody == null) {
                Log.d(
                    "ai_call",
                    "Response body is null after retrofit call. " +
                            "Message: ${response.message()}." +
                            "Error body: ${response.errorBody()?.string()}."
                )

                streamingFlow.emit(
                    AiRepository.StreamingState.Error(
                        Exception(
                            "Response body is null after retrofit call. " +
                                    "Message: ${response.message()}." +
                                    "Error body: ${response.errorBody()?.string()}."
                        )
                    )
                )
                return@launch
            }

            val chunks = try {
                mapResponseToChunks(responseBody)
            } catch (e: Exception) {
                streamingFlow.emit(AiRepository.StreamingState.Error(e))
                return@launch
            }

            ensureActive()

            var messageText = ""
            chunks.forEachIndexed { index, openAiChunk ->
                messageText += openAiChunk.choices.first().delta!!.content
                streamingFlow.emit(AiRepository.StreamingState.Streaming(messageText))
                delay(DELAY_BETWEEN_CHUNKS_MS)
                ensureActive()
            }

            streamingFlow.emit(AiRepository.StreamingState.Finished(messageText))
        }

        return streamingFlow
    }


    private fun prepareListOfMessages(
        character: Character, history: List<ChatMessage>
    ): List<OpenAiMessage> {
        val trainingMessages = listOf(
            OpenAiMessage(
                role = "system",
                content = "Lets assume you and the user are having a chat conversation. " +
                        "You are ${character.name}." +
                        "You should text to me in short messages and sometimes " +
                        "crack jokes about yourself or other characters from your world. " +
                        "Don't mention that you are AI model." +
                        "Remember, keep messages short."
            ),
//            OpenAiMessage("user", "Hi, ${character.name}. How's it going?"),
//            OpenAiMessage("assistant", "All good. You?"),
//            OpenAiMessage("user", "What are you doing right now?"),
//            OpenAiMessage("assistant", "Well, nothing very much, just partying with Iron Man."),
        )

        val historyMessages = history.takeLast(20).map {
            OpenAiMessage(
                role = when (it.sender) {
                    Sender.User -> "user"
                    Sender.Character -> "assistant"
                },
                content = it.messageText,
            )
        }

        return trainingMessages + historyMessages
    }


    private fun mapResponseToChunks(response: String): List<OpenAiChunk> {
        val gson = Gson()
        val chunks = mutableListOf<OpenAiChunk>()

        val parts = response.split("data:")
            .map { it.trim() }.filter { it.isNotBlank() }

        parts.forEachIndexed { index, part ->
            Log.d("tag_open_ai", "parse [$index]: $part")
            if (part.trim() == "[DONE]") {
                return@forEachIndexed
            }


            val chunk = gson.fromJson(part.trim(), OpenAiChunk::class.java)
            if (chunk.choices.first().finish_reason == "stop") {
                return@forEachIndexed
            }

            chunks.add(chunk)
        }

        return chunks
    }
}