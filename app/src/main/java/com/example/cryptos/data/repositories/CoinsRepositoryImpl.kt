package com.example.cryptos.data.repositories

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import com.example.cryptos.core.networking.safeCall
import com.example.cryptos.core.networking.toDomain
import com.example.cryptos.core.other.TAG
import com.example.cryptos.data.local.room.CoinsDao
import com.example.cryptos.data.local.room.DataBase
import com.example.cryptos.data.local.room.entities.CoinRoomEntity
import com.example.cryptos.data.local.room.entities.CoinsDataBaseMapper.mapToLocalEntityList
import com.example.cryptos.data.network.ApiService
import com.example.cryptos.data.network.entities.mappers.CoinListDomainMapper
import com.example.cryptos.domain.models.CoinDomain
import com.example.cryptos.domain.repositories.CoinsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CoinsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val coinsDao: CoinsDao,
    private val dataBase: DataBase,
    @ApplicationContext private val context: Context
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
        val coin = coinsDao.getCoinById(coinId)
        coin?.let {
            coinsDao.updateFavoriteStatus(coinId, !it.isFavorite)
        }
    }

    override suspend fun getCoinById(coinId: String): CoinRoomEntity? {
        try {
            val coin = coinsDao.getCoinById(coinId)
            return coin
        } catch (e: Throwable) {
            Log.d(TAG, "getCoinById: failed, error = $e ")
            return null
        }
    }

    private suspend fun insertCoinsToDB(list: List<CoinDomain>) {
        try {
            dataBase.withTransaction {
                val favoriteCoinsList = coinsDao.getAllCoins().first()
                    .filter { it.isFavorite == true }
                    .map { it.id }
                coinsDao.deleteAllCoins()
                coinsDao.insertAllCoins(list.mapToLocalEntityList())
                coinsDao.getAllCoins().first().forEach {
                    if (it.id in favoriteCoinsList) {
                        coinsDao.updateFavoriteStatus(it.id, true)
                    }
                }
            }
            Log.d(TAG, "insertCoinsToDB: success")
        } catch (e: Throwable) {
            Log.e(TAG, "insertCoinsToDB: failed, \nerror = $e")
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