package com.jeksonsoftsolutions.cryptos.di.modules

import com.jeksonsoftsolutions.cryptos.data.local.room.UserProfileDao
import com.jeksonsoftsolutions.cryptos.data.repositories.UserProfileRepositoryImpl
import com.jeksonsoftsolutions.cryptos.domain.repositories.UserProfileRepository
import com.jeksonsoftsolutions.cryptos.domain.usecases.profile.GetUserProfileUseCase
import com.jeksonsoftsolutions.cryptos.domain.usecases.profile.ProfileUseCases
import com.jeksonsoftsolutions.cryptos.domain.usecases.profile.UpdateUserProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
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

    @Provides
    @Singleton
    fun provideUserProfileRepository(userProfileDao: UserProfileDao): UserProfileRepository {
        return UserProfileRepositoryImpl(userProfileDao)
    }
}