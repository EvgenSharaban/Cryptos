package com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models

data class CoinsListItemUI(
    val id: String,
    val rank: String,
    val fullName: String,
    val shortName: String,
    val price: String,
    val marketCapUsd: String,
    val changePercent24Hr: String,
    val isFavorite: Boolean,
)