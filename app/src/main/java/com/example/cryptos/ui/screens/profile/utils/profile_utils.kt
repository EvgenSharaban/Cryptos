package com.example.cryptos.ui.screens.profile.utils


import android.content.Context
import com.example.cryptos.R
import com.example.cryptos.ui.screens.Events
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay

suspend fun <T> Channel<Events>.runWithInternetCheckAndErrorHandling(
    context: Context,
    checkInternet: suspend () -> Boolean,
    block: suspend () -> T
) {
    try {
        if (!checkInternet()) {
            delay(300)
            val title = context.getString(R.string.network_unavailable_error)
            val description = context.getString(R.string.connect_to_internet_and_try_again)
            this.send(Events.MessageForUser(title, description))
        }
    } catch (e: Exception) {
        val title = context.getString(R.string.unknown_error)
        val description = e.localizedMessage ?: context.getString(R.string.unknown_error)
        this.send(Events.MessageForUser(title, description))
        null
    }
    block()
}