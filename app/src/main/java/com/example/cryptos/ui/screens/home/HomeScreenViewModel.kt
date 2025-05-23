package com.example.cryptos.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.example.cryptos.R
import com.example.cryptos.domain.repositories.CoinsRepository
import com.example.cryptos.ui.components.LoadResult
import com.example.cryptos.ui.screens.base.BaseViewModel
import com.example.cryptos.ui.screens.home.models.CoinsUiMapper.mapToUiList
import com.example.cryptos.ui.screens.home.models.HomeScreenItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val coinsRepository: CoinsRepository,
) : BaseViewModel() {

    val stateFlow: StateFlow<LoadResult<ScreenState>> = coinsRepository.coinsLocal
        .map {
            if (it.isEmpty()) {
                LoadResult.Empty(R.string.no_coins_found)
            } else {
                LoadResult.Success(ScreenState(it.mapToUiList()))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LoadResult.Loading
        )

    init {
//        fetchCoins()
    }

    fun fetchCoins() {
        viewModelScope.launch {
            coinsRepository.fetchCoins()
        }
    }

    fun toggleFavorite(coinId: String) {
        viewModelScope.launch {
            coinsRepository.toggleFavorite(coinId)
        }
    }

    data class ScreenState(
        val items: List<HomeScreenItem>
    )
}