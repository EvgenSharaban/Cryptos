package com.jeksonsoftsolutions.cryptos.data.repositories

import android.util.Log
import androidx.room.withTransaction
import com.jeksonsoftsolutions.cryptos.core.networking.safeCall
import com.jeksonsoftsolutions.cryptos.core.networking.toDomain
import com.jeksonsoftsolutions.cryptos.core.other.TAG
import com.jeksonsoftsolutions.cryptos.data.local.room.CoinsDao
import com.jeksonsoftsolutions.cryptos.data.local.room.DataBase
import com.jeksonsoftsolutions.cryptos.data.local.room.entities.CoinRoomEntity
import com.jeksonsoftsolutions.cryptos.data.local.room.entities.CoinsDataBaseMapper.mapToLocalEntityList
import com.jeksonsoftsolutions.cryptos.data.network.ApiService
import com.jeksonsoftsolutions.cryptos.data.network.entities.mappers.CoinListDomainMapper
import com.jeksonsoftsolutions.cryptos.domain.models.CoinDomain
import com.jeksonsoftsolutions.cryptos.domain.repositories.CoinsRepository
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListItemUI
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListItemUiMapper.mapToUiList
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.CoinsSortType
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortDirection
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CoinsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val coinsDao: CoinsDao,
    private val dataBase: DataBase,
//    @ApplicationContext private val context: Context
) : CoinsRepository {

    override fun coinsLocal(sortState: SortState?, searchQuery: String): Flow<List<CoinsListItemUI>> {
        return coinsDao.getAllCoins().map { coins ->
            val uiCoins = coins.mapToUiList()
            val filteredCoins = if (searchQuery.isNotEmpty()) {
                uiCoins.filter { coin ->
                    coin.fullName.contains(searchQuery, ignoreCase = true) ||
                            coin.shortName.contains(searchQuery, ignoreCase = true)
                }
            } else {
                uiCoins
            }

            if (sortState != null) {
                sortCoins(filteredCoins, sortState)
            } else {
                filteredCoins
            }
        }
    }

    override fun favoriteCoinsLocal(sortState: SortState?): Flow<List<CoinsListItemUI>> {
        return coinsDao.getFavoriteCoins().map { coins ->
            val uiCoins = coins.mapToUiList()
            if (sortState != null) {
                sortCoins(uiCoins, sortState)
            } else {
                uiCoins
            }
        }
    }

    override suspend fun fetchCoins(): Result<Unit> {
        return safeCall {
            apiService.getCoins()
        }
            .toDomain(CoinListDomainMapper)
            .onSuccess {
                insertCoinsToDB(it.data)
                Log.d(TAG, "getCoins: success, size = ${it.data.size}")
            }
            .onFailure { error ->
                Log.e(TAG, "getCoins(): failure, \nerror = $error")
            }
            .map { }

//        fakeInsert() // need for test
//        return Result
//            .success {}
//            .map {  }

//        return Result  // need for test
//            .failure(LoadDataException(context.getString(R.string.failed_to_load_data_error)))
    }

    override suspend fun toggleFavorite(coinId: String) {
        val coin = coinsDao.getCoinById(coinId) ?: return
        coinsDao.updateFavoriteStatus(coinId, !coin.isFavorite)
    }

    override suspend fun getCoinById(coinId: String): CoinRoomEntity? {
        try {
            return coinsDao.getCoinById(coinId)
        } catch (e: Throwable) {
            Log.d(TAG, "getCoinById: failed, error = $e ")
            return null
        }
    }

    private suspend fun insertCoinsToDB(list: List<CoinDomain>) {
        try {
            dataBase.withTransaction {
                val favoriteCoinsList = coinsDao.getFavoriteCoins().first()
                    .map { it.id }

                coinsDao.deleteAllCoins()
                coinsDao.insertAllCoins(list.mapToLocalEntityList())

                if (favoriteCoinsList.isNotEmpty()) {
                    coinsDao.updateFavoriteStatusBatch(favoriteCoinsList)
                }
            }
            Log.d(TAG, "insertCoinsToDB: success")
        } catch (e: Exception) {
            Log.e(TAG, "insertCoinsToDB: failed", e)
        }
    }

    private fun sortCoins(coins: List<CoinsListItemUI>, sortState: SortState): List<CoinsListItemUI> {
        val sorted = when (sortState.type) {
            CoinsSortType.RANK -> coins.sortedBy { it.rank.toIntOrNull() ?: Int.MAX_VALUE }
            CoinsSortType.NAME -> coins.sortedBy { it.shortName }
            CoinsSortType.PRICE -> coins.sortedBy {
                it.price
                    .replace("$", "")
                    .replace(",", "") // need for formats like US (1,256.01)
                    .toDoubleOrNull() ?: 0.0
            }
        }
        return if (sortState.direction == SortDirection.DESCENDING) sorted.reversed() else sorted
    }

//    private suspend fun fakeInsert() { // need for test
//        try {
//            dataBase.withTransaction {
//                val favoriteCoinsList = coinsDao.getAllCoins().first().filter {
//                    it.isFavorite == true
//                }.map { it.id }
//                Log.d(TAG, "fakeInsert: favoriteList = $favoriteCoinsList")
//                delay(2000)
//                coinsDao.deleteAllCoins()
//            }
//        } catch (e: Throwable) {
//
//        }
//    }

}