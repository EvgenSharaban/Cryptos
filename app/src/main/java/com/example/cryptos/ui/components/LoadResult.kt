package com.example.cryptos.ui.components

sealed class LoadResult<out T> {
    data object Loading : LoadResult<Nothing>()
    data class Success<T>(val data: T) : LoadResult<T>()
    data class Error(val exception: Exception) : LoadResult<Nothing>()
}