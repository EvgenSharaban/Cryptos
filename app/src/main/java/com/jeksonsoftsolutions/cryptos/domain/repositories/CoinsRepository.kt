package com.jeksonsoftsolutions.cryptos.domain.repositories

import com.jeksonsoftsolutions.cryptos.data.local.room.entities.CoinRoomEntity
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {

    val coinsLocal: Flow<List<CoinRoomEntity>>
    val favoriteCoinsLocal: Flow<List<CoinRoomEntity>>

    suspend fun fetchCoins(): Result<Unit>
    suspend fun toggleFavorite(coinId: String)
    suspend fun getCoinById(coinId: String): CoinRoomEntity?
}