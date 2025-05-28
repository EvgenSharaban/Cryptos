package com.jeksonsoftsolutions.cryptos.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.jeksonsoftsolutions.cryptos.data.local.room.entities.CoinRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinsDao {

    @Query("SELECT * FROM coins")
    fun getAllCoins(): Flow<List<CoinRoomEntity>>

    @Query("SELECT * FROM coins WHERE id = :coinId")
    suspend fun getCoinById(coinId: String): CoinRoomEntity?

    @Query("SELECT * FROM coins WHERE isFavorite = 1")
    fun getFavoriteCoins(): Flow<List<CoinRoomEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertAllCoins(coins: List<CoinRoomEntity>)

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()

    @Query("UPDATE coins SET isFavorite = :isFavorite WHERE id = :coinId")
    suspend fun updateFavoriteStatus(coinId: String, isFavorite: Boolean)

}