package com.jeksonsoftsolutions.cryptos.domain.usecases.profile

import com.jeksonsoftsolutions.cryptos.domain.models.UserProfile
import com.jeksonsoftsolutions.cryptos.domain.repositories.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    operator fun invoke(): Flow<UserProfile> {
        return repository.getUserProfile()
    }
}
