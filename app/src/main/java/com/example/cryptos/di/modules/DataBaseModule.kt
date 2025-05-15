package com.example.cryptos.di.modules

import android.content.Context
import com.example.cryptos.data.local.room.CoinsDao
import com.example.cryptos.data.local.room.DataBase
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
}