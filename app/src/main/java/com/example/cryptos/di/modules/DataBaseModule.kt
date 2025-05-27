package com.example.cryptos.di.modules

import android.content.Context
import com.example.cryptos.data.local.room.CoinsDao
import com.example.cryptos.data.local.room.DataBase
import com.example.cryptos.data.local.room.UserProfileDao
import com.example.cryptos.data.repositories.UserProfileRepositoryImpl
import com.example.cryptos.domain.repositories.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): DataBase {
        return DataBase.getDataBase(context)
    }

    @Provides
    fun provideCoinsDao(dataBase: DataBase): CoinsDao {
        return dataBase.coinsDao()
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(userProfileDao: UserProfileDao): UserProfileRepository {
        return UserProfileRepositoryImpl(userProfileDao)
    }

    @Provides
    @Singleton
    fun provideUserProfileDao(db: DataBase): UserProfileDao {
        return db.userProfileDao()
    }

}