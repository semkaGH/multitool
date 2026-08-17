package com.multitool.app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object ApiClient {
    
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"
    private const val OPENROUTER_BASE_URL = "https://openrouter.ai/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    fun getGeminiRetrofit(apiKey: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .client(okHttpClient.newBuilder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build()
                    chain.proceed(request)
                }
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    fun getOpenRouterRetrofit(apiKey: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OPENROUTER_BASE_URL)
            .client(okHttpClient.newBuilder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $apiKey")
                        .addHeader("Content-Type", "application/json")
                        .build()
                    chain.proceed(request)
                }
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface GeminiApi {
    // Gemini API interface - to be implemented
}

interface OpenRouterApi {
    // OpenRouter API interface - to be implemented
}
