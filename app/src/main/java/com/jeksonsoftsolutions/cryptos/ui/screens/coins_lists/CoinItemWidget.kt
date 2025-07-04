package com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.core.other.TAG
import com.jeksonsoftsolutions.cryptos.core.other.formatNumber
import com.jeksonsoftsolutions.cryptos.core.other.roundTo
import com.jeksonsoftsolutions.cryptos.ui.components.RoundImageCoinAvatar
import com.jeksonsoftsolutions.cryptos.ui.screens.LocalNavAnimatedContentScope
import com.jeksonsoftsolutions.cryptos.ui.screens.LocalSharedTransitionScope
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListItemUI
import com.jeksonsoftsolutions.cryptos.ui.theme.DarkGreen
import com.jeksonsoftsolutions.cryptos.ui.theme.itemCoinBackground
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CoinItemWidget(
    item: CoinsListItemUI,
    onConClicked: (String) -> Unit,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    Card(
        modifier = modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onConClicked(item.id)
                }
            ),
//        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.itemCoinBackground)
                .padding(top = 16.dp, bottom = 16.dp, start = 4.dp, end = 8.dp)
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
            RoundImageCoinAvatar(
                logo = item.shortName,
                modifier = Modifier.size(32.dp),
            )
            Spacer(Modifier.size(16.dp))
            Column {
                Text(text = item.shortName)
                val marketCap = formatNumber(safeCastStringToDouble(item.marketCapUsd))
                Text(
                    text = stringResource(R.string.price_value, marketCap),
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.size(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                val roundedPrice = item.price.toDouble().roundTo(2).toString()
                with(sharedTransitionScope) {
                    Text(
                        text = stringResource(R.string.price_value, roundedPrice),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.sharedBounds(
                            sharedTransitionScope.rememberSharedContentState(key = "coin_price_${item.id}"),
                            animatedVisibilityScope = animatedContentScope,
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                        )
                    )
                }
                PercentageChangeText(
                    safeCastStringToDouble(item.changePercent24Hr),
                    itemId = item.id,
                    sharedTransitionScope,
                    animatedContentScope
                )
            }
            Spacer(Modifier.size(8.dp))
            IconButton(onClick = onFavoriteClicked) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = if (item.isFavorite) stringResource(R.string.remove_from_favorites) else stringResource(R.string.add_to_favorites),
                    tint = if (item.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PercentageChangeText(
    value: Double,
    itemId: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val arrow = if (value >= 0) stringResource(R.string.arrow_up) else stringResource(R.string.arrow_down)
    val color = if (value >= 0) DarkGreen else Color.Red
    val formattedValue = String.format(Locale.US, "%.2f%%", abs(value))

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = color)) {
            append("$arrow $formattedValue")
        }
    }

    with(sharedTransitionScope) {
        Text(
            text = annotatedString,
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.sharedBounds(
                sharedTransitionScope.rememberSharedContentState(key = "coin_change_${itemId}"),
                animatedVisibilityScope = animatedContentScope,
                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
            ),
        )
    }
}

private fun safeCastStringToDouble(value: String): Double {
    try {
        return value.toDouble()
    } catch (e: Exception) {
        Log.e(TAG, "safeCastStringToDouble: error = $e")
        return 0.0
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showSystemUi = true)
@Composable
private fun CoinItemWidgetItemPreview() {
    SharedTransitionLayout {
        AnimatedContent(targetState = false) {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this@SharedTransitionLayout,
                LocalNavAnimatedContentScope provides this
            ) {
                CoinItemWidget(
                    item = CoinsListItemUI(
                        id = "001",
                        fullName = "Bitcoin",
                        shortName = "BTC",
                        price = "104215.561",
                        rank = "100",
                        marketCapUsd = "1134454375148.0439441877343485",
                        changePercent24Hr = "0.894",
                        isFavorite = it
                    ),
                    onConClicked = {},
                    onFavoriteClicked = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this,
                )
            }
        }
    }
}