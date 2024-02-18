package com.lvsmsmch.lchat.data.server

import com.google.gson.GsonBuilder
import com.lvsmsmch.lchat.utils.LOADING_TIMEOUT_SECONDS
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitBuilder<API_TYPE>(
    private val apiClass: Class<API_TYPE>,
    private val baseUrl: String
) {
    fun build(): API_TYPE {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(LOADING_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(LOADING_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(apiClass)
    }
}