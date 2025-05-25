package com.example.cryptos.domain.usecases

interface CheckConnectionUseCase {

    fun isConnectedToNetwork(): Boolean

}