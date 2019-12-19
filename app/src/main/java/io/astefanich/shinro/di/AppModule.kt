package io.astefanich.shinro.di

import dagger.Binds
import dagger.Module
import io.astefanich.shinro.repository.BoardRepository
import io.astefanich.shinro.repository.FakeBoardRepository
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Binds
    internal abstract fun providesBoardRepository(board: FakeBoardRepository): BoardRepository

}