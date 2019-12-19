package io.astefanich.shinro.di

import dagger.Module
import dagger.Provides
import io.astefanich.shinro.repository.BoardRepository

@Module
abstract class AppModule {

    @Module
    companion object {

        @Provides
        internal fun providesBoardRepository(): BoardRepository = BoardRepository()
    }
}