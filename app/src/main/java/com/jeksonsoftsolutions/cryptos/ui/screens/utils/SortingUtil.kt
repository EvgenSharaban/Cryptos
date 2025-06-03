package com.jeksonsoftsolutions.cryptos.ui.screens.utils

data class SortState(
    val type: SortType = SortType.RANK,
    val direction: SortDirection = SortDirection.ASCENDING
)

enum class SortType {
    RANK, NAME, PRICE
}

enum class SortDirection {
    ASCENDING, DESCENDING
}