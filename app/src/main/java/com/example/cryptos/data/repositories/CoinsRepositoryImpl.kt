package com.example.cryptos.data.repositories

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
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val coinsDao: CoinsDao,
    private val dataBase: DataBase,
) : CoinsRepository {

    override val coinsLocal: Flow<List<CoinRoomEntity>> = coinsDao.getAllCoins()

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
    }

    private suspend fun insertCoinsToDB(list: List<CoinDomain>) {
        try {
            dataBase.withTransaction {
                coinsDao.deleteAllCoins()
                coinsDao.insertAllCoins(list.mapToLocalEntityList())
            }
            Log.d(TAG, "insertCoinsToDB: success")
        } catch (e: Throwable) {
            Log.e(TAG, "insertCoinsToDB: failed, \nerror = $e")
        }
    }

}