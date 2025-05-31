package com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.ui.scaffold.AppScaffold
import com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.CoinsGraph.CoinDetailsRoute
import com.jeksonsoftsolutions.cryptos.ui.screens.Events
import com.jeksonsoftsolutions.cryptos.ui.screens.Events.MessageForUser
import com.jeksonsoftsolutions.cryptos.ui.screens.LocalNavController
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.ItemsListContent

@Composable
fun HomeScreen() {
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val screenState by viewModel.stateFlow.collectAsState()
    val event by viewModel.event.collectAsState()

    val navController = LocalNavController.current

    AppScaffold(
        title = stringResource(R.string.coins_screen),
        showNavigationUp = false
    ) { paddingValues ->
        ItemsListContent(
            getLoadResult = { screenState },
            onItemClicked = { id ->
                navController.navigate(route = CoinDetailsRoute(id))
            },
            onFavoriteClicked = { id ->
                viewModel.toggleFavorite(id)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onTryAgainAction = viewModel::fetchCoins,
        )
    }


    when (event) {
        is MessageForUser -> {
            AlertDialog(
                onDismissRequest = { viewModel.clearEvent() },
                title = { Text((event as MessageForUser).messageTitle) },
                text = {
                    if ((event as MessageForUser).messageDescription.isNotEmpty()) {
                        Text((event as MessageForUser).messageDescription)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // delayTime need for displaying progress bar, but can do without it
                            viewModel.fetchCoins(300)
                            viewModel.clearEvent()
                        }
                    ) {
                        Text(stringResource(R.string.try_again))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.clearEvent() }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }

        Events.None -> {}
    }

}