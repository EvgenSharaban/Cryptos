package com.jeksonsoftsolutions.cryptos.di.modules

import com.jeksonsoftsolutions.cryptos.data.local.room.UserProfileDao
import com.jeksonsoftsolutions.cryptos.data.repositories.UserProfileRepositoryImpl
import com.jeksonsoftsolutions.cryptos.domain.repositories.UserProfileRepository
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
    fun provideUserProfileRepository(userProfileDao: UserProfileDao): UserProfileRepository {
        return UserProfileRepositoryImpl(userProfileDao)
    }

}