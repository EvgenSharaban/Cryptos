package com.jeksonsoftsolutions.cryptos.ui.screens

sealed interface Events {
    data class MessageForUser(
        val messageTitle: String,
        val messageDescription: String,
    ) : Events

    data object None : Events
}