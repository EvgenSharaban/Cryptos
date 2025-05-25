package com.example.cryptos.ui.screens.coins_lists

sealed interface EventsForCoinsLists {
    class MessageForUser(
        val messageTitle: String,
        val messageDescription: String,
    ) : EventsForCoinsLists
}