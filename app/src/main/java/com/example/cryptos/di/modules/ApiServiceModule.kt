package com.example.cryptos.di.modules

import com.example.cryptos.data.network.ApiService
import com.example.cryptos.data.network.RetrofitClient
import com.example.cryptos.data.network.interceptors.HttpLogger
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ApiServiceModule {

    @Binds
    fun provideHttpLogger(httpLogger: HttpLogger): HttpLoggingInterceptor.Logger

    companion object {

        @Provides
        fun provideHttpLoggingInterceptor(
            httpLogger: HttpLoggingInterceptor.Logger
        ): HttpLoggingInterceptor = HttpLoggingInterceptor(httpLogger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        @Provides
        @Singleton
        fun provideApiService(
            httpLoggingInterceptor: HttpLoggingInterceptor,
        ): ApiService {
            return RetrofitClient(httpLoggingInterceptor).create()
        }
    }
}