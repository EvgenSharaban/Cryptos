package com.jeksonsoftsolutions.cryptos.di.modules

import android.content.Context
import com.jeksonsoftsolutions.cryptos.data.local.room.CoinsDao
import com.jeksonsoftsolutions.cryptos.data.local.room.DataBase
import com.jeksonsoftsolutions.cryptos.data.local.room.UserProfileDao
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
    fun provideUserProfileDao(db: DataBase): UserProfileDao {
        return db.userProfileDao()
    }

}