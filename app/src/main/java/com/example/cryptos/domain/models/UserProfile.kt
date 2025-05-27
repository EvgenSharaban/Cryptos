package com.example.cryptos.domain.models

data class UserProfile(
    val username: String = "",
    val email: String = "",
    val bio: String = "",
    val avatarUri: String? = null
)
