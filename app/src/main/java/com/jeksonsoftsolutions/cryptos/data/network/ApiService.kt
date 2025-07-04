package com.jeksonsoftsolutions.cryptos.data.network

import com.jeksonsoftsolutions.cryptos.data.network.entities.CoinListEntity
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/v3/assets")
    suspend fun getCoins(): Response<CoinListEntity>

}