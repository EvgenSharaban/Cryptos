package com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.domain.repositories.CoinsRepository
import com.jeksonsoftsolutions.cryptos.ui.components.LoadResult
import com.jeksonsoftsolutions.cryptos.ui.components.NoInternetException
import com.jeksonsoftsolutions.cryptos.ui.screens.Events
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListItemUiMapper.mapToUiList
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListScreenState
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

    private val _event = MutableStateFlow<Events?>(null)
    val event = _event.asStateFlow()

    init {
        initiateCacheData()
        fetchCoins()
    }

    fun fetchCoins(delayTime: Long? = null) {
        viewModelScope.launch {
            _stateFlow.value = LoadResult.Loading
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
                _stateFlow.value = LoadResult.Success(CoinsListScreenState(coinsLocalList.mapToUiList()))
            }
        }
    }

    fun toggleFavorite(coinId: String) {
        viewModelScope.launch {
            coinsRepository.toggleFavorite(coinId)
        }
    }

    fun clearEvent() {
        _event.update { null }
    }

    private fun initiateCacheData() {
        viewModelScope.launch {
            coinsRepository.coinsLocal.collect { coins ->
                _stateFlow.update {
                    if (coins.isEmpty()) {
                        LoadResult.Empty(R.string.no_coins_found)
                    } else {
                        LoadResult.Success(CoinsListScreenState(coins.mapToUiList()))
                    }
                }
            }
        }
    }
}