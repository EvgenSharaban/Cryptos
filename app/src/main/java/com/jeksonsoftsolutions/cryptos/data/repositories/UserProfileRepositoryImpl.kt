package com.jeksonsoftsolutions.cryptos.data.repositories

import com.jeksonsoftsolutions.cryptos.data.local.room.UserProfileDao
import com.jeksonsoftsolutions.cryptos.data.local.room.entities.UserProfileEntity
import com.jeksonsoftsolutions.cryptos.domain.models.UserProfile
import com.jeksonsoftsolutions.cryptos.domain.repositories.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao
) : UserProfileRepository {

    // Default profile to use when there's no data in the database
    private val defaultProfile = UserProfile(
        username = "Crypto User",
        email = "user@example.com",
        bio = "Crypto enthusiast and investor"
    )

    override fun getUserProfile(): Flow<UserProfile> {
        return userProfileDao.getUserProfile().map { entity ->
            entity?.toUserProfile() ?: defaultProfile
        }
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        val entity = UserProfileEntity.fromUserProfile(profile)
        userProfileDao.insertUserProfile(entity)
    }
}