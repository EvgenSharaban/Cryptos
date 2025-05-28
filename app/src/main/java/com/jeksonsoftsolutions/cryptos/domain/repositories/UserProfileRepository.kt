package com.jeksonsoftsolutions.cryptos.domain.repositories

import com.jeksonsoftsolutions.cryptos.domain.models.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getUserProfile(): Flow<UserProfile>
    suspend fun updateUserProfile(profile: UserProfile)
}