package com.example.cryptos.data.network

import com.example.cryptos.data.network.interceptors.ApiKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitClient @Inject constructor(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    apiKeyInterceptor: ApiKeyInterceptor,
) {

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(apiKeyInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun create(): ApiService = retrofit.create()

    companion object {
        private const val BASE_URL = "https://rest.coincap.io"
    }
}