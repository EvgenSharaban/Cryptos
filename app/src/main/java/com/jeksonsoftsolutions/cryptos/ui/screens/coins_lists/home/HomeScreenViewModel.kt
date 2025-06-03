package com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.domain.repositories.CoinsRepository
import com.jeksonsoftsolutions.cryptos.ui.components.LoadResult
import com.jeksonsoftsolutions.cryptos.ui.components.NoInternetException
import com.jeksonsoftsolutions.cryptos.ui.screens.Events
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListItemUI
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListItemUiMapper.mapToUiList
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListScreenState
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortDirection
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortState
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val coinsRepository: CoinsRepository,
    @ApplicationContext val context: Context,
) : ViewModel() {

    private var _stateFlow = MutableStateFlow<LoadResult<CoinsListScreenState>>(LoadResult.Loading)
    val stateFlow: StateFlow<LoadResult<CoinsListScreenState>> = _stateFlow.asStateFlow()

    private val _event = MutableStateFlow<Events>(Events.None)
    val event = _event.asStateFlow()

    private val _sortState = MutableStateFlow(SortState())
    val sortState = _sortState.asStateFlow()

    private val _showSortRow = MutableStateFlow(false)
    val showSortRow = _showSortRow.asStateFlow()

    private var currentCoins: List<CoinsListItemUI> = emptyList()


    init {
        initiateCacheData()
        fetchCoins()
    }

    fun fetchCoins(delayTime: Long? = null) {
        viewModelScope.launch {
            _stateFlow.update { LoadResult.Loading }
            if (delayTime != null && delayTime > 0) delay(delayTime)
            coinsRepository.fetchCoins().onFailure { exception ->
                // TODO if LoadResult.Error doesn't need delete it and everything connected with it
//                _stateFlow.value = LoadResult.Error(exception as Exception)
                val coinsLocalList = coinsRepository.coinsLocal.first()
                val title = exception.message ?: context.getString(R.string.unknown_error)
                val description = if (exception.cause is NoInternetException) {
                    context.getString(R.string.connect_to_internet_and_try_again)
                } else {
                    ""
                }
                _event.update {
                    Events.MessageForUser(
                        messageTitle = title,
                        messageDescription = description
                    )
                }
                currentCoins = coinsLocalList.mapToUiList()
                _stateFlow.update {
                    when (val currentState = _stateFlow.value) {
                        is LoadResult.Success -> LoadResult.Success(CoinsListScreenState(sortCoins(currentCoins)))
                        else -> currentState
                    }
                }
            }
        }
    }

    fun toggleFavorite(coinId: String) {
        viewModelScope.launch {
            coinsRepository.toggleFavorite(coinId)
        }
    }

    fun clearEvent() {
        _event.update { Events.None }
    }

    fun toggleSortRowVisibility() {
        _showSortRow.update { !it }
    }

    fun sortBy(sortType: SortType) {
        if (sortState.value.type == sortType) {
            // Toggle direction if same field
            _sortState.update { currentState ->
                currentState.copy(
                    direction = if (currentState.direction == SortDirection.ASCENDING) {
                        SortDirection.DESCENDING
                    } else {
                        SortDirection.ASCENDING
                    }
                )
            }
        } else {
            // New field, reset to ascending
            _sortState.update {
                SortState(type = sortType, direction = SortDirection.ASCENDING)
            }
        }

        val sortedCoins = sortCoins(currentCoins)
        _stateFlow.update {
            when (val currentState = _stateFlow.value) {
                is LoadResult.Success -> LoadResult.Success(CoinsListScreenState(sortedCoins))
                else -> currentState
            }
        }
    }

    private fun sortCoins(coins: List<CoinsListItemUI>): List<CoinsListItemUI> {
        val sorted = when (sortState.value.type) {
            SortType.RANK -> coins.sortedBy { it.rank.toIntOrNull() ?: Int.MAX_VALUE }
            SortType.NAME -> coins.sortedBy { it.shortName }
            SortType.PRICE -> coins.sortedBy {
                it.price
                    .replace("$", "")
                    .toDoubleOrNull()
                    ?: 0.0
            }
        }

        return if (sortState.value.direction == SortDirection.DESCENDING) {
            sorted.reversed()
        } else {
            sorted
        }
    }

    private fun initiateCacheData() {
        viewModelScope.launch {
            coinsRepository.coinsLocal.collect { coins ->
                currentCoins = coins.mapToUiList()
                val sortedCoins = sortCoins(currentCoins)
                _stateFlow.update {
                    if (coins.isEmpty()) {
                        LoadResult.Empty(R.string.no_coins_found)
                    } else {
                        LoadResult.Success(CoinsListScreenState(sortedCoins))
                    }
                }
            }
        }
    }
}