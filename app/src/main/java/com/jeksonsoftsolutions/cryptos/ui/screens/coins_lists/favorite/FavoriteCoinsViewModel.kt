package com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.domain.repositories.CoinsRepository
import com.jeksonsoftsolutions.cryptos.ui.components.LoadResult
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListItemUiMapper.mapToUiList
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.models.CoinsListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCoinsViewModel @Inject constructor(
    val coinsRepository: CoinsRepository,
) : ViewModel() {

    val stateFlow: StateFlow<LoadResult<CoinsListScreenState>> = coinsRepository.favoriteCoinsLocal
        .map {
            if (it.isEmpty()) {
                LoadResult.Empty(R.string.no_coins_found)
            } else {
                LoadResult.Success(CoinsListScreenState(it.mapToUiList()))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LoadResult.Loading
        )


    fun toggleFavorite(coinId: String) {
        viewModelScope.launch {
            coinsRepository.toggleFavorite(coinId)
        }
    }

}