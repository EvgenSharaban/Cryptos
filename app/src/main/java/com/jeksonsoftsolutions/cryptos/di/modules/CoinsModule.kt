package com.jeksonsoftsolutions.cryptos.di.modules

import com.jeksonsoftsolutions.cryptos.data.repositories.CoinsRepositoryImpl
import com.jeksonsoftsolutions.cryptos.domain.repositories.CoinsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoinsModule {

    @Binds
    abstract fun provideCoinsRepository(coinsRepositoryImpl: CoinsRepositoryImpl): CoinsRepository

}