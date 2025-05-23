package com.example.cryptos.ui.screens.coins_lists

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cryptos.ui.components.LoadResult
import com.example.cryptos.ui.components.LoadResultContent
import com.example.cryptos.ui.screens.coins_lists.models.CoinsListScreenState

@Composable
fun ItemsContent(
    getLoadResult: () -> LoadResult<CoinsListScreenState>,
    onItemClicked: (String) -> Unit,
    onFavoriteClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    onTryAgainAction: () -> Unit = {},
) {
    LoadResultContent(
        loadResult = getLoadResult(),
        content = { screenState ->
            LazyColumn(
                modifier = modifier
            ) {
                items(items = screenState.items) { item ->
                    CoinItemWidget(
                        item = item,
                        onConClicked = { id ->
                            onItemClicked(id)
                        },
                        onFavoriteClicked = {
                            onFavoriteClicked(item.id)
                        }
                    )
                }
            }
        },
        onTryAgainAction = onTryAgainAction,
        modifier = modifier
    )
}