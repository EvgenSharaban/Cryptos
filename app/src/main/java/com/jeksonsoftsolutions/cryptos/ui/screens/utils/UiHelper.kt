package com.jeksonsoftsolutions.cryptos.ui.screens.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.ui.screens.Events
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

suspend fun MutableStateFlow<Events?>.handleNetConnectionError(
    context: Context,
    checkInternet: () -> Boolean,
) {
    if (!checkInternet()) {
        delay(300)
        val title = context.getString(R.string.network_unavailable_error)
        val description = context.getString(R.string.connect_to_internet_and_try_again)
        this.update {
            Events.MessageForUser(title, description)
        }
    }
}

@Composable
fun <T> ObserveAsChannelEvents(flow: Flow<T>, onEvent: suspend (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}