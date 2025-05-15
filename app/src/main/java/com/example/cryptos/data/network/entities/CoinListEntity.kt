package com.example.cryptos.data.network.entities

data class CoinListEntity(
    val timestamp: Int? = null,
    val data: List<CoinEntity>? = null,
)

data class CoinEntity(
    val id: String? = null,
    val rank: String? = null,
    val symbol: String? = null,
    val name: String? = null,
    val supply: String? = null,
    val maxSupply: String? = null,
    val marketCapUsd: String? = null,
    val volumeUsd24Hr: String? = null,
    val priceUsd: String? = null,
    val changePercent24Hr: String? = null,
    val vwap24Hr: String? = null,
    val explorer: String? = null,
)