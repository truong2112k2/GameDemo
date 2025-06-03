package com.example.testgame.di

import com.example.testgame.data.impl.GameRepositoryImpl
import com.example.testgame.data.repository.IGameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideGameRepository(gameRepositoryImpl: GameRepositoryImpl): IGameRepository

}