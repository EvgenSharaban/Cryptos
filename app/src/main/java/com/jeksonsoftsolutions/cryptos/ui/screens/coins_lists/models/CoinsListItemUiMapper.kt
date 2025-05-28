package com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models

import com.jeksonsoftsolutions.cryptos.data.local.room.entities.CoinRoomEntity

object CoinsListItemUiMapper {

    fun CoinRoomEntity.mapToUi(): CoinsListItemUI {
        return CoinsListItemUI(
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

    fun List<CoinRoomEntity>.mapToUiList(): List<CoinsListItemUI> =
        this.map { it.mapToUi() }
}