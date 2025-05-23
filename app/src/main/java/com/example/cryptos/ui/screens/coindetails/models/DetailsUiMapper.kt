package com.example.cryptos.ui.screens.coindetails.models

import com.example.cryptos.data.local.room.entities.CoinRoomEntity

object DetailsUiMapper {

    fun CoinRoomEntity.mapToUi(): DetailsUiModel {
        return DetailsUiModel(
            id = this.id,
            rank = this.rank,
            symbol = this.symbol,
            name = this.name,
            maxSupply = this.maxSupply,
            priceUsd = if (this.priceUsd.isNotEmpty()) this.priceUsd.toDouble() else 0.0,
            changePercent24Hr = if (this.changePercent24Hr.isNotEmpty()) this.changePercent24Hr.toDouble() else 0.0,
            marketCap = if (this.marketCapUsd.isNotEmpty()) this.marketCapUsd.toDouble() else 0.0,
            supply = if (this.supply.isNotEmpty()) this.supply.toDouble() else 0.0,
            volumeUsd24Hr = if (this.volumeUsd24Hr.isNotEmpty()) this.volumeUsd24Hr.toDouble() else 0.0,
            isFavorite = this.isFavorite
        )
    }
}