package com.example.cryptos.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptos.R
import com.example.cryptos.ui.components.LoadResult
import com.example.cryptos.ui.components.LoadResultContent
import com.example.cryptos.ui.scaffold.AppScaffold
import com.example.cryptos.ui.scaffold.environment.CoinsGraph.CoinDetailsRoute
import com.example.cryptos.ui.screens.LocalNavController
import com.example.cryptos.ui.screens.home.HomeScreenViewModel.ScreenState

@Composable
fun HomeScreen() {
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val screenState = viewModel.stateFlow.collectAsState()
    val navController = LocalNavController.current

    AppScaffold(
        titleRes = R.string.coins_screen,
        showNavigationUp = false
    ) { paddingValues ->
        ItemsContent(
            getLoadResult = { screenState.value },
            onItemClicked = { id ->
                navController.navigate(route = CoinDetailsRoute(id))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onTryAgainAction = viewModel::fetchCoins
        )
    }

}

@Composable
fun ItemsContent(
    getLoadResult: () -> LoadResult<ScreenState>,
    onItemClicked: (String) -> Unit,
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
                        }
                    )
                }
            }
        },
        onTryAgainAction = onTryAgainAction,
        modifier = modifier
    )
}