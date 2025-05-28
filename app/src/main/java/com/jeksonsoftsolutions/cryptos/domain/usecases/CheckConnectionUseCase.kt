package com.jeksonsoftsolutions.cryptos.domain.usecases

interface CheckConnectionUseCase {

    fun isConnectedToNetwork(): Boolean

}