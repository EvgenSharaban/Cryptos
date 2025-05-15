package com.example.cryptos.core.networking

import com.google.gson.annotations.SerializedName

data class ErrorsEntity(
    @SerializedName("error") val error: String? = null,
)
