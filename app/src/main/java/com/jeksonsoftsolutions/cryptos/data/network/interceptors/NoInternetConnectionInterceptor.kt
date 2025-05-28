package com.jeksonsoftsolutions.cryptos.data.network.interceptors

import android.content.Context
import android.util.Log
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.core.other.TAG
import com.jeksonsoftsolutions.cryptos.domain.usecases.CheckConnectionUseCase
import com.jeksonsoftsolutions.cryptos.ui.components.NoInternetException
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class NoInternetConnectionInterceptor @Inject constructor(
    private val checkConnectionUseCase: CheckConnectionUseCase,
    @ApplicationContext private val context: Context,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (checkConnectionUseCase.isConnectedToNetwork().not()) {
            noInternetError(
                exception = NoInternetException(),
                message = context.getString(R.string.network_unavailable_error)
            )
        }
        return try {
            chain.proceed(chain.request())
        } catch (e: UnknownHostException) {
            noInternetError(e)
        } catch (_: SocketTimeoutException) {
            throw SocketTimeoutException("Timeout exception")
        }
    }

    private fun noInternetError(exception: Throwable? = null, message: String? = null): Nothing {
        Log.d(TAG, "noInternetError11: cause = $exception, \nmessage = $message")
        throw NoInternetException(cause = exception, message = message)
    }

}