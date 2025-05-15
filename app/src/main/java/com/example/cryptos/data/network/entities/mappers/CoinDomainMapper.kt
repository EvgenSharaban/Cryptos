package com.example.cryptos.data.network.entities.mappers

import com.example.cryptos.data.network.entities.CoinEntity
import com.example.cryptos.data.network.entities.CoinListEntity
import com.example.cryptos.domain.models.CoinDomain
import com.example.cryptos.domain.models.CoinListDomain

object CoinListDomainMapper : FromEntityToDomainMapper<CoinListEntity, CoinListDomain> {

    override fun mapToDomain(entity: CoinListEntity?): CoinListDomain {
        return CoinListDomain(
            timestamp = entity?.timestamp ?: 0,
            data = mapToDomainList(entity?.data)
        )
    }

    private fun mapToDomainList(list: List<CoinEntity?>?): List<CoinDomain> {
        val listResult = if (list.isNullOrEmpty()) {
            emptyList()
        } else {
            list.filterNotNull()
        }
        return listResult.map {
            mapToDomain(it)
        }
    }

    private fun mapToDomain(entity: CoinEntity?): CoinDomain {
        return CoinDomain(
            id = entity?.id ?: "",
            rank = entity?.rank ?: "",
            symbol = entity?.symbol ?: "",
            name = entity?.name ?: "",
            supply = entity?.supply ?: "",
            maxSupply = entity?.maxSupply ?: "",
            marketCapUsd = entity?.marketCapUsd ?: "",
            volumeUsd24Hr = entity?.volumeUsd24Hr ?: "",
            priceUsd = entity?.priceUsd ?: "",
            changePercent24Hr = entity?.changePercent24Hr ?: "",
            vwap24Hr = entity?.vwap24Hr ?: "",
            explorer = entity?.explorer ?: ""
        )
    }

}

