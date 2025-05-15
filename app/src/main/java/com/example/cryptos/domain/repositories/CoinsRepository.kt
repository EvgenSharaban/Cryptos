package com.example.cryptos.domain.repositories

import com.example.cryptos.data.local.room.entities.CoinRoomEntity
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {

    val coinsLocal: Flow<List<CoinRoomEntity>>

    suspend fun fetchCoins(): Result<Unit>
}