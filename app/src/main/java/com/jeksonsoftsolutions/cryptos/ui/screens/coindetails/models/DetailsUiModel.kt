package com.jeksonsoftsolutions.cryptos.ui.screens.coindetails.models

import java.text.NumberFormat
import java.util.Locale

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
) {
    val formattedMarketCap = formatCurrency(marketCap)
    val formattedVolumeUsd24Hr = formatCurrency(volumeUsd24Hr)
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(value)
}
