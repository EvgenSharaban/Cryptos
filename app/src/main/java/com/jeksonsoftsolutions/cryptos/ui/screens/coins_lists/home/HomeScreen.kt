package com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.ui.scaffold.AppScaffold
import com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.CoinsGraph.CoinDetailsRoute
import com.jeksonsoftsolutions.cryptos.ui.screens.Events
import com.jeksonsoftsolutions.cryptos.ui.screens.Events.MessageForUser
import com.jeksonsoftsolutions.cryptos.ui.screens.LocalNavController
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.ItemsListContent
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortDirection
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortState
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortType

@Composable
fun HomeScreen() {
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val screenState by viewModel.stateFlow.collectAsState()
    val event by viewModel.event.collectAsState()
    val sortState by viewModel.sortState.collectAsState()
    val showSortRow by viewModel.showSortRow.collectAsState()

    val navController = LocalNavController.current

    AppScaffold(
        title = stringResource(R.string.coins_screen),
        showNavigationUp = false,
        toolbarAction = {
            Row {
                IconButton(onClick = {
                    // TODO: Implement search functionality
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
                IconButton(onClick = {
                    viewModel.toggleSortRowVisibility()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Sort,
                        contentDescription = null
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (showSortRow) {
                SortRow(
                    currentSortState = sortState,
                    onSortClicked = viewModel::sortBy
                )
            }

            ItemsListContent(
                getLoadResult = { screenState },
                onItemClicked = { id ->
                    navController.navigate(route = CoinDetailsRoute(id))
                },
                onFavoriteClicked = { id ->
                    viewModel.toggleFavorite(id)
                },
                modifier = Modifier.fillMaxSize(),
                onTryAgainAction = viewModel::fetchCoins,
            )
        }
    }

    when (val currentEvent = event) {
        is MessageForUser -> {
            AlertDialog(
                onDismissRequest = { viewModel.clearEvent() },
                title = { Text(currentEvent.messageTitle) },
                text = {
                    if (currentEvent.messageDescription.isNotEmpty()) {
                        Text(currentEvent.messageDescription)
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

@Composable
private fun SortRow(
    currentSortState: SortState,
    onSortClicked: (SortType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SortField(
            text = "Rank",
            sortType = SortType.RANK,
            isSelected = currentSortState.type == SortType.RANK,
            direction = if (currentSortState.type == SortType.RANK) currentSortState.direction else SortDirection.ASCENDING,
            onClick = onSortClicked,
            modifier = Modifier.weight(1f)
        )

        SortField(
            text = "Name",
            sortType = SortType.NAME,
            isSelected = currentSortState.type == SortType.NAME,
            direction = if (currentSortState.type == SortType.NAME) currentSortState.direction else SortDirection.ASCENDING,
            onClick = onSortClicked,
            modifier = Modifier.weight(2f)
        )

        SortField(
            text = "Price",
            sortType = SortType.PRICE,
            isSelected = currentSortState.type == SortType.PRICE,
            direction = if (currentSortState.type == SortType.PRICE) currentSortState.direction else SortDirection.ASCENDING,
            onClick = onSortClicked,
            modifier = Modifier.weight(1.5f)
        )
    }
}

@Composable
private fun SortField(
    text: String,
    sortType: SortType,
    isSelected: Boolean,
    direction: SortDirection,
    onClick: (SortType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onClick(sortType) }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
        if (isSelected) {
            Icon(
                imageVector = if (direction == SortDirection.ASCENDING) {
                    Icons.Default.KeyboardArrowUp
                } else {
                    Icons.Default.KeyboardArrowDown
                },
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}