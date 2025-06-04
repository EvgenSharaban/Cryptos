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
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortDirection
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortState
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortType
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

    override val coinsLocal: Flow<List<CoinRoomEntity>> = coinsDao.getAllCoins()
    override val favoriteCoinsLocal: Flow<List<CoinRoomEntity>>
        get() = coinsDao.getFavoriteCoins()

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

    override fun getSortedCoins(sortState: SortState): Flow<List<CoinsListItemUI>> {
        return coinsLocal.map { coins ->
            val uiCoins = coins.mapToUiList()
            val sorted = when (sortState.type) {
                SortType.RANK -> uiCoins.sortedBy { it.rank.toIntOrNull() ?: Int.MAX_VALUE }
                SortType.NAME -> uiCoins.sortedBy { it.shortName }
                SortType.PRICE -> uiCoins.sortedBy {
                    it.price
                        .replace("$", "")
                        .replace(",", "") // need for formats like US (1,256.01)
                        .toDoubleOrNull() ?: 0.0
                }
            }
            if (sortState.direction == SortDirection.DESCENDING) sorted.reversed() else sorted
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