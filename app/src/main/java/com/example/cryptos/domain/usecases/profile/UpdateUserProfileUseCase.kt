package com.example.cryptos.domain.usecases.profile

import com.example.cryptos.domain.models.UserProfile
import com.example.cryptos.domain.repositories.UserProfileRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(profile: UserProfile) {
        repository.updateUserProfile(profile)
    }
}
