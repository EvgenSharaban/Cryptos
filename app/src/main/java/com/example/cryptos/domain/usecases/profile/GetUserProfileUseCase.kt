package com.example.cryptos.domain.usecases.profile

import com.example.cryptos.domain.models.UserProfile
import com.example.cryptos.domain.repositories.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    operator fun invoke(): Flow<UserProfile> {
        return repository.getUserProfile()
    }
}
