package com.jeksonsoftsolutions.cryptos.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeksonsoftsolutions.cryptos.domain.models.UserProfile

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1, // Since we'll only have one user profile
    val username: String,
    val email: String,
    val bio: String,
    val avatarUri: String?
) {
    fun toUserProfile(): UserProfile {
        return UserProfile(
            username = username,
            email = email,
            bio = bio,
            avatarUri = avatarUri
        )
    }

    companion object {
        fun fromUserProfile(profile: UserProfile): UserProfileEntity {
            return UserProfileEntity(
                username = profile.username,
                email = profile.email,
                bio = profile.bio,
                avatarUri = profile.avatarUri
            )
        }
    }
}
