package com.wingspan.adminpanel.api

import com.wingspan.adminpanel.api.EndPointUrlProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BaseUrlProvider {

    val BASE_URL="https://new-app-9a2b.onrender.com/"
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
    fun create(): EndPointUrlProvider {
        val retrofit=Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(EndPointUrlProvider::class.java)
    }
    fun getInstance(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Optional for logging network calls
            .build()

        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}