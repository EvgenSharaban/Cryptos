package com.example.cryptos.ui.screens.home.models

data class HomeScreenItem(
    val id: String,
    val rank: String,
    val fullName: String,
    val shortName: String,
    val price: String,
    val marketCapUsd: String,
    val changePercent24Hr: String,
)