package com.example.cryptos.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptos.R
import com.example.cryptos.core.other.TAG
import com.example.cryptos.core.other.formatNumber
import com.example.cryptos.core.other.roundTo
import com.example.cryptos.ui.components.RoundImage
import com.example.cryptos.ui.screens.home.models.HomeScreenItem
import com.example.cryptos.ui.theme.CryptosTheme
import com.example.cryptos.ui.theme.DarkGreen
import com.example.cryptos.ui.theme.itemCoinBackground
import java.util.Locale
import kotlin.math.abs

@Composable
fun CoinItemWidget(
    item: HomeScreenItem,
    onConClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(
                onClick = {
                    onConClicked(item.id)
                }
            )
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
//        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.itemCoinBackground)
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.rank,
                textAlign = TextAlign.Center,
                maxLines = 1,
                fontSize = 14.sp,
                modifier = Modifier.width(26.dp),
            )
            Spacer(Modifier.size(8.dp))
            RoundImage(
                logo = item.shortName,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.size(8.dp))
            Column {
                Text(text = item.shortName)
                val marketCap = formatNumber(safeCastStringToDouble(item.marketCapUsd))
                Text(
                    text = stringResource(R.string.price_value, marketCap),
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.size(12.dp))
            val roundedPrice = item.price.toDouble().roundTo(2).toString()
            Text(
                text = stringResource(R.string.price_value, roundedPrice),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            PercentageChangeText(safeCastStringToDouble(item.changePercent24Hr))
        }
    }
}

private fun safeCastStringToDouble(value: String): Double {
    try {
        val castingValue = value.toDouble()
        return castingValue
    } catch (e: Throwable) {
        Log.e(TAG, "safeCastStringToDouble: error = $e")
        throw e
    }
}

@Composable
private fun PercentageChangeText(value: Double) {
    val arrow = if (value >= 0) "▲" else "▼"
    val color = if (value >= 0) DarkGreen else Color.Red
    val formattedValue = String.format(Locale.US, "%.2f%%", abs(value))

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = color)) {
            append("$arrow $formattedValue")
        }
    }

    Text(
        text = annotatedString,
        fontSize = 16.sp,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showSystemUi = true)
@Composable
private fun CoinItemWidgetItemPreview() {
    CryptosTheme {
        Scaffold {
            Column(
                modifier = Modifier.padding(it)
            ) {
                CoinItemWidget(
                    item = HomeScreenItem(
                        id = "1",
                        fullName = "Bitcoin",
                        shortName = "BTC",
                        price = "104215.561",
                        rank = "100",
                        marketCapUsd = "1134454375148.0439441877343485",
                        changePercent24Hr = "0.894"
                    ),
                    onConClicked = {},
                )
            }
        }
    }
}