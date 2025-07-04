package com.jeksonsoftsolutions.cryptos.ui.screens.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// TODO delete if doesn't need
abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

}