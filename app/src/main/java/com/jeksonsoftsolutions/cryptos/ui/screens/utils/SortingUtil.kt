package com.jeksonsoftsolutions.cryptos.ui.screens.utils

data class SortState(
    val type: CoinsSortType = CoinsSortType.RANK,
    val direction: SortDirection = SortDirection.ASCENDING
)

enum class CoinsSortType {
    RANK, NAME, PRICE
}

enum class SortDirection {
    ASCENDING, DESCENDING
}