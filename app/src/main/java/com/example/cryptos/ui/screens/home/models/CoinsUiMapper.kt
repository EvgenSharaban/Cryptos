package com.example.cryptos.ui.screens.home.models

import com.example.cryptos.data.local.room.entities.CoinRoomEntity

object CoinsUiMapper {

    fun CoinRoomEntity.mapToUi(): HomeScreenItem {
        return HomeScreenItem(
            id = this.id,
            rank = this.rank,
            fullName = this.name,
            shortName = this.symbol,
            price = this.priceUsd,
            marketCapUsd = this.marketCapUsd,
            changePercent24Hr = this.changePercent24Hr,
            isFavorite = this.isFavorite,
        )
    }

    fun List<CoinRoomEntity>.mapToUiList(): List<HomeScreenItem> =
        this.map { it.mapToUi() }
}