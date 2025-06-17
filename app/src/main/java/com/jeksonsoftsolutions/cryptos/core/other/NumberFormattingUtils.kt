package com.jeksonsoftsolutions.cryptos.core.other

import java.util.Locale

fun Double.roundTo(numbersAfterPoint: Int): Double {
    return String.format(Locale.US, "%.${numbersAfterPoint}f", this).toDouble()
}

fun formatNumber(value: Double): String {
    val absValue = kotlin.math.abs(value)
    val suffixes = listOf(
        1_000_000_000_000.0 to "T",
        1_000_000_000.0 to "B",
        1_000_000.0 to "M"
    )

    for ((threshold, suffix) in suffixes) {
        if (absValue >= threshold) {
            val scaled = value / threshold
            return String.format(Locale.US, "%.2f %s", scaled, suffix)
        }
    }

    return String.format(Locale.US, "%.2f", value)
}