package com.jeksonsoftsolutions.cryptos.ui.screens.coindetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.core.other.roundTo
import com.jeksonsoftsolutions.cryptos.ui.components.LoadResultContent
import com.jeksonsoftsolutions.cryptos.ui.scaffold.AppScaffold
import com.jeksonsoftsolutions.cryptos.ui.screens.coindetails.models.DetailsUiModel
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.PercentageChangeText

@Composable
fun CoinDetailsScreen() {
    val viewModel: CoinDetailsViewModel = hiltViewModel()
    val screenState = viewModel.stateFlow.collectAsState()

    LoadResultContent(
        loadResult = screenState.value,
        content = { screenState ->
            AppScaffold(
                title = screenState.coin.name,
                showNavigationUp = true,
                toolbarAction = {
                    screenState.coin.let { coin ->
                        IconButton(onClick = { viewModel.toggleFavorite(coin.id) }) {
                            Icon(
                                imageVector = if (coin.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (coin.isFavorite) stringResource(R.string.remove_from_favorites) else stringResource(R.string.add_to_favorites),
                                tint = if (coin.isFavorite) Color.Red else LocalContentColor.current
                            )
                        }
                    }
                }
            ) { paddingValues ->
                ContentItem(
                    screenState.coin,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    )
}

@Composable
private fun ContentItem(
    coin: DetailsUiModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Price section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row {
                    Text(
                        text = stringResource(R.string.current_price),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(R.string.rank_, coin.rank),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                RoundedPriceText(coin.priceUsd)
                PercentageChangeText(coin.changePercent24Hr)
            }
        }

        // Market data section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.market_data),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                DataRow(stringResource(R.string.market_cap), coin.formattedMarketCap)
                DataRow(stringResource(R.string.supply), "${coin.supply}")
                DataRow(stringResource(R.string._24h_volume), coin.formattedVolumeUsd24Hr)
                DataRow(stringResource(R.string.symbol), coin.symbol)
            }
        }
    }
}


@Composable
private fun RoundedPriceText(
    value: Double,
) {
    val price = value.roundTo(2).toString()
    Text(
        text = stringResource(R.string.price_value, price),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun DataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
}