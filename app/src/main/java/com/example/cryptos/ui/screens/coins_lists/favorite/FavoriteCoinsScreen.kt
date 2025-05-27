package com.example.cryptos.ui.screens.coins_lists.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptos.R
import com.example.cryptos.ui.scaffold.AppScaffold
import com.example.cryptos.ui.scaffold.environment.FavoriteGraph.FavoriteCoinDetailsRoute
import com.example.cryptos.ui.screens.LocalNavController
import com.example.cryptos.ui.screens.coins_lists.ItemsListContent

@Composable
fun FavoriteCoinsScreen() {
    val viewModel: FavoriteCoinsViewModel = hiltViewModel()
    val screenState = viewModel.stateFlow.collectAsState()
    val navController = LocalNavController.current

    AppScaffold(
        title = stringResource(R.string.favorite_coins_screen),
        showNavigationUp = false,
    ) { paddingValues ->
        ItemsListContent(
            getLoadResult = { screenState.value },
            onItemClicked = { id ->
                navController.navigate(route = FavoriteCoinDetailsRoute(id))
            },
            onFavoriteClicked = { id ->
                viewModel.toggleFavorite(id)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        )
    }
}