package com.example.cryptos.di.modules

import com.example.cryptos.data.network.ApiService
import com.example.cryptos.data.network.RetrofitClient
import com.example.cryptos.data.network.interceptors.ApiKeyInterceptor
import com.example.cryptos.data.network.interceptors.HttpLoggerInterceptor
import com.example.cryptos.data.network.interceptors.NoInternetConnectionInterceptor
import com.example.cryptos.data.usecases.CheckConnectionUseCaseImpl
import com.example.cryptos.domain.usecases.CheckConnectionUseCase
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
    fun provideHttpLogger(httpLoggerInterceptor: HttpLoggerInterceptor): HttpLoggingInterceptor.Logger

    @Singleton
    @Binds
    fun provideCheckConnection(
        checkConnectionUseCaseImpl: CheckConnectionUseCaseImpl
    ): CheckConnectionUseCase

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
            apiKeyInterceptor: ApiKeyInterceptor,
            noInternetConnectionInterceptor: NoInternetConnectionInterceptor,
        ): ApiService {
            return RetrofitClient(
                httpLoggingInterceptor,
                apiKeyInterceptor,
                noInternetConnectionInterceptor
            ).create()
        }
    }
}