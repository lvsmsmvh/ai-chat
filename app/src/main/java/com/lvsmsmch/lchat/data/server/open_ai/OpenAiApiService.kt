package com.lvsmsmch.lchat.data.server.open_ai

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApiService {
    @POST("v1/chat/completions")
    fun getChatCompletions(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Body request: OpenAiRequest
    ): Call<OpenAiResponse>


    @POST("v1/chat/completions")
    fun getChatCompletionsStream(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Body request: OpenAiRequestStream
    ):  Call<String>
}