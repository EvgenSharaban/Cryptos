package com.example.cryptos.ui.screens.coindetails.models

data class DetailsUiModel(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val maxSupply: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val marketCap: Double,
    val supply: Double,
    val volumeUsd24Hr: Double,
    val isFavorite: Boolean,
)
