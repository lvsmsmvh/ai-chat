package com.lvsmsmch.lchat.data.server.open_ai

data class OpenAiRequestStream(
    val model: String,
    val messages: List<OpenAiMessage>,
    val temperature: Double,
    val stream: Boolean = true,
)
