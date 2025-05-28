package com.jeksonsoftsolutions.cryptos.data.local.room.entities

import com.jeksonsoftsolutions.cryptos.domain.models.CoinDomain

object CoinsDataBaseMapper {

    fun CoinDomain.mapToLocalEntity(): CoinRoomEntity {
        return CoinRoomEntity(
            id = this.id,
            rank = this.rank,
            symbol = this.symbol,
            name = this.name,
            supply = this.supply,
            maxSupply = this.maxSupply,
            marketCapUsd = this.marketCapUsd,
            volumeUsd24Hr = this.volumeUsd24Hr,
            priceUsd = this.priceUsd,
            changePercent24Hr = this.changePercent24Hr,
            vwap24Hr = this.vwap24Hr,
            explorer = this.explorer,
            isFavorite = this.isFavorite,
        )
    }

    fun List<CoinDomain>.mapToLocalEntityList(): List<CoinRoomEntity> =
        this.map { it.mapToLocalEntity() }

}