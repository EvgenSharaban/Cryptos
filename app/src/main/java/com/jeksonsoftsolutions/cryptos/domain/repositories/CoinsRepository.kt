package com.jeksonsoftsolutions.cryptos.domain.repositories

import com.jeksonsoftsolutions.cryptos.data.local.room.entities.CoinRoomEntity
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListItemUI
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortState
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {

    fun coinsLocal(sortState: SortState? = null, searchQuery: String = ""): Flow<List<CoinsListItemUI>>
    fun favoriteCoinsLocal(sortState: SortState? = null): Flow<List<CoinsListItemUI>>

    // TODO may be there search not need, discuss
    suspend fun fetchCoins(search: String? = null, limit: Int? = null): Result<Unit>
    suspend fun toggleFavorite(coinId: String)
    suspend fun getCoinById(coinId: String): CoinRoomEntity?

}