package com.example.cryptos.di.modules

import com.example.cryptos.domain.repositories.UserProfileRepository
import com.example.cryptos.domain.usecases.profile.GetUserProfileUseCase
import com.example.cryptos.domain.usecases.profile.ProfileUseCases
import com.example.cryptos.domain.usecases.profile.UpdateUserProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(repository: UserProfileRepository): GetUserProfileUseCase {
        return GetUserProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserProfileUseCase(repository: UserProfileRepository): UpdateUserProfileUseCase {
        return UpdateUserProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideProfileUseCases(
        getUserProfileUseCase: GetUserProfileUseCase,
        updateUserProfileUseCase: UpdateUserProfileUseCase
    ): ProfileUseCases {
        return ProfileUseCases(
            getUserProfile = getUserProfileUseCase,
            updateUserProfile = updateUserProfileUseCase
        )
    }
}