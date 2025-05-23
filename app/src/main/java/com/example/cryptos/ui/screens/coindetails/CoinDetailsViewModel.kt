package com.example.cryptos.ui.screens.coindetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.cryptos.domain.repositories.CoinsRepository
import com.example.cryptos.ui.components.LoadResult
import com.example.cryptos.ui.screens.base.BaseViewModel
import com.example.cryptos.ui.screens.coindetails.models.DetailsUiMapper.mapToUi
import com.example.cryptos.ui.screens.coindetails.models.DetailsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailsViewModel @Inject constructor(
    val coinsRepository: CoinsRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val _stateFlow = MutableStateFlow<LoadResult<ScreenState>>(LoadResult.Loading)
    val stateFlow: StateFlow<LoadResult<ScreenState>> = _stateFlow.asStateFlow()

    init {
        savedStateHandle.get<String>("id")?.let { coinId ->
            loadCoin(coinId)
        }
    }

    fun loadCoin(coinId: String) {
        viewModelScope.launch {
            val coin = coinsRepository.getCoinById(coinId)
            if (coin != null) {
                _stateFlow.value = LoadResult.Success(ScreenState(coin.mapToUi()))
            }
        }
    }

    fun toggleFavorite(coinId: String) {
        viewModelScope.launch {
            coinsRepository.toggleFavorite(coinId)
            loadCoin(coinId)
        }
    }

    data class ScreenState(
        val coin: DetailsUiModel
    )
}