package com.lvsmsmch.lchat.data.server.open_ai

data class OpenAiResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val usage: Usage,
    val choices: List<Choice>
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class Choice(
    val message: OpenAiMessage,
    val logprobs: Any?,
    val finish_reason: String,
    val index: Int
)