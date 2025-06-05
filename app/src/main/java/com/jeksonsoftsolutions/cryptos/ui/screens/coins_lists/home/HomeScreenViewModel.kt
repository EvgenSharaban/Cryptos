package com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.domain.repositories.CoinsRepository
import com.jeksonsoftsolutions.cryptos.ui.components.LoadResult
import com.jeksonsoftsolutions.cryptos.ui.components.NoInternetException
import com.jeksonsoftsolutions.cryptos.ui.screens.Events
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListScreenState
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.CoinsSortType
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortDirection
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.SortState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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

    private val _searchQueryState = MutableStateFlow("")
    val searchQueryState = _searchQueryState.asStateFlow()

    private val _showSortRow = MutableStateFlow(false)
    val showSortRow = _showSortRow.asStateFlow()

    private val _showSearchRow = MutableStateFlow(false)
    val showSearchRow = _showSearchRow.asStateFlow()

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

    fun toggleSearchRowVisibility() {
        _showSearchRow.update { !it }
        if (!_showSearchRow.value) {
            _searchQueryState.update { "" }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQueryState.update { query }
    }

    fun sortBy(coinsSortType: CoinsSortType) {
        _sortState.update { currentState ->
            if (currentState.type == coinsSortType) {
                // Toggle direction if same field
                currentState.copy(
                    direction = if (currentState.direction == SortDirection.ASCENDING) {
                        SortDirection.DESCENDING
                    } else {
                        SortDirection.ASCENDING
                    }
                )
            } else {
                // New field, reset to ascending
                SortState(type = coinsSortType, direction = SortDirection.ASCENDING)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initiateCacheData() {
        viewModelScope.launch {
            /** .flatMapLatest ensures that: when sortState or searchQuery changes, the previous data stream is discarded;
             * a new stream is created with updated sorting and search parameters;
             * the UI receives correctly sorted and filtered data!
             * In combine lambda we pack both values in Flow (this can be done also using data class)
             * In flatMapLatest we unpack them and pass to repository
             * */
            combine(sortState, searchQueryState) { sort, search ->
                Pair(sort, search)
            }.flatMapLatest { (currentSortState, currentSearchQuery) ->
                coinsRepository.coinsLocal(currentSortState, currentSearchQuery)
            }
                .collect { sortedCoins ->
                    _stateFlow.update {
                        if (sortedCoins.isEmpty()) {
                            LoadResult.Empty(R.string.no_coins_found)
                        } else {
                            LoadResult.Success(CoinsListScreenState(sortedCoins))
                        }
                    }
                }
        }
    }
}