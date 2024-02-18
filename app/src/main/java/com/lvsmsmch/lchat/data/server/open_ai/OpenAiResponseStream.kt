package com.lvsmsmch.lchat.data.server.open_ai

data class OpenAiChunk(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val system_fingerprint: String,
    val usage: Usage,
    val choices: List<ChoiceStream>
)

data class ChoiceStream(
    val index: Int,
    val delta: OpenAiMessageStream?,
    val finish_reason: String?,
)

data class OpenAiMessageStream(
    val content: String
)