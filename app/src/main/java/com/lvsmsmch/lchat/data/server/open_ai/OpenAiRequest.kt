package com.lvsmsmch.lchat.data.server.open_ai

data class OpenAiRequest(
    val model: String,
    val messages: List<OpenAiMessage>,
    val temperature: Double,
)
