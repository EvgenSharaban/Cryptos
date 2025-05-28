package com.jeksonsoftsolutions.cryptos.domain.usecases.profile

import javax.inject.Inject

data class ProfileUseCases @Inject constructor(
    val getUserProfile: GetUserProfileUseCase,
    val updateUserProfile: UpdateUserProfileUseCase
)