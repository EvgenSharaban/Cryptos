package com.example.cryptos.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val authorizedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $TOKEN")
            .build()

        return chain.proceed(authorizedRequest)
    }

    companion object {
        private const val TOKEN = "68581b34256492d2d3accca5646d36ab5a6cac5726c3ab94cb29f25f148bd56b" // second token from 18.05.25, first - created 14.05.25
    }
}