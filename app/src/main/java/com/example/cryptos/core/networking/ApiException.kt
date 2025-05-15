package com.example.cryptos.core.networking

import java.io.IOException

class ApiException(
    message: String? = null,
    val code: Int,
) : IOException(message) {

    override fun toString(): String {
        return "\nerror: $message, \nStatus code of error: $code"
    }
}