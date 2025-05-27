package com.example.cryptos.ui.screens

sealed interface Events {
    data class MessageForUser(
        val messageTitle: String,
        val messageDescription: String,
    ) : Events
}