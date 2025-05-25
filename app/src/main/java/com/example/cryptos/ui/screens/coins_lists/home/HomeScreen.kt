package com.example.cryptos.ui.screens.coins_lists.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptos.R
import com.example.cryptos.ui.scaffold.AppScaffold
import com.example.cryptos.ui.scaffold.environment.CoinsGraph.CoinDetailsRoute
import com.example.cryptos.ui.screens.LocalNavController
import com.example.cryptos.ui.screens.coins_lists.EventsForCoinsLists.MessageForUser
import com.example.cryptos.ui.screens.coins_lists.ItemsContent

@Composable
fun HomeScreen() {
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val screenState = viewModel.stateFlow.collectAsState()
    val navController = LocalNavController.current

    var userMessage by remember { mutableStateOf<MessageForUser?>(null) }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is MessageForUser -> {
                    userMessage = MessageForUser(event.messageTitle, event.messageDescription)
                }
            }
        }
    }

    AppScaffold(
        title = stringResource(R.string.coins_screen),
        showNavigationUp = false
    ) { paddingValues ->
        ItemsContent(
            getLoadResult = { screenState.value },
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

    if (userMessage != null) {
        AlertDialog(
            onDismissRequest = { userMessage = null },
            title = { Text(userMessage?.messageTitle ?: stringResource(R.string.unknown_error)) },
            text = {
                if (userMessage?.messageDescription?.isNotEmpty() == true) {
                    Text(userMessage?.messageDescription ?: stringResource(R.string.unknown_error))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // delayTime need for displaying progress bar, but can do without it
                        viewModel.fetchCoins(500)
                        userMessage = null
                    }
                ) {
                    Text(stringResource(R.string.try_again))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { userMessage = null }
                ) {
                    Text(stringResource(R.string.cansel))
                }
            }
        )
    }

}