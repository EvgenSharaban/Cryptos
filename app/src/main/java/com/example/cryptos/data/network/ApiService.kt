package com.example.cryptos.data.network

import com.example.cryptos.data.network.entities.CoinListEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v3/assets")
    suspend fun getCoins(
        @Query("limit") limit: Int = 100
    ): Response<CoinListEntity>

}