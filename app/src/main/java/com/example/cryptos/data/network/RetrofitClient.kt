package com.example.cryptos.data.network

import javax.inject.Inject

class RetrofitClient @Inject constructor() {



    companion object {
        private const val BASE_URL = "https://rest.coincap.io"
    }
}