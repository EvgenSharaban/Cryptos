package com.example.cryptos.ui.components

import java.io.IOException

class NoInternetException(
    override val cause: Throwable? = null,
    override val message: String? = cause?.message
) : IOException()