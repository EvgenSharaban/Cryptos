package com.jeksonsoftsolutions.cryptos.data.usecases

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.jeksonsoftsolutions.cryptos.domain.usecases.CheckConnectionUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CheckConnectionUseCaseImpl @Inject constructor(
    @ApplicationContext context: Context,
) : CheckConnectionUseCase {

    private val connectivityManager: ConnectivityManager? = context.getSystemService<ConnectivityManager>()

    override fun isConnectedToNetwork(): Boolean {
        connectivityManager ?: return false

        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            .isNetworkCapabilitiesValid()
    }

    private fun NetworkCapabilities?.isNetworkCapabilitiesValid(): Boolean {
        this ?: return false
        val hasInternetCapability = hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val hasValidated = hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        val hasTransport = hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        return hasInternetCapability && hasValidated && hasTransport
    }
}