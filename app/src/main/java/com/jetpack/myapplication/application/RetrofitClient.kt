package com.jetpack.myapplication.application

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://api.trakt.tv/"
    private const val CLIENT_ID = " 1d20f217c21df5cf312e5573405389a01762550b757c216fce492919351764ed"// Replace with your actual key
    private const val API_VERSION = "2"               // Check Trakt docs for the correct version

    // Interceptor to add required headers
    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("trakt-api-key", CLIENT_ID)
            .addHeader("trakt-api-version", API_VERSION)
            .build()
        chain.proceed(request)
    }

    // Optional: a logging interceptor for debugging purposes
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: Retrofit by lazy {
        // 1. Create a Moshi instance
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory()) // For Kotlin data classes
            // Add any custom adapters here if needed
            .build()

        // 2. Use the Moshi instance in MoshiConverterFactory.create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}