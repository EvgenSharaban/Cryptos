package com.example.cryptos.domain.models

data class CoinListDomain(
    val timestamp: Long,
    val data: List<CoinDomain>,
)

data class CoinDomain(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String,
    val maxSupply: String,
    val marketCapUsd: String,
    val volumeUsd24Hr: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val vwap24Hr: String,
    val explorer: String,
)
