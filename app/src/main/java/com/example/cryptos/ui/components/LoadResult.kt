package com.example.cryptos.ui.components

import androidx.annotation.StringRes

sealed class LoadResult<out T> {
    data object Loading : LoadResult<Nothing>()
    data class Success<T>(val data: T) : LoadResult<T>()
    data class Empty(@StringRes val message: Int) : LoadResult<Nothing>()

    // TODO if LoadResult.Error doesn't need delete it and everything connected with it
    data class Error(val exception: Exception) : LoadResult<Nothing>()
}