package com.jeksonsoftsolutions.cryptos.domain.repositories

import com.jeksonsoftsolutions.cryptos.data.local.room.entities.CoinRoomEntity
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListItemUI
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortState
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {

    fun coinsLocal(sortState: SortState? = null): Flow<List<CoinsListItemUI>>
    fun favoriteCoinsLocal(sortState: SortState? = null): Flow<List<CoinsListItemUI>>

    suspend fun fetchCoins(): Result<Unit>
    suspend fun toggleFavorite(coinId: String)
    suspend fun getCoinById(coinId: String): CoinRoomEntity?

}