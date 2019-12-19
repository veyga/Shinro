package io.astefanich.shinro.di

import android.app.Application
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.database.AppDatabase
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.repository.BoardRepository
import io.astefanich.shinro.repository.FakeBoardRepository
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Binds
    internal abstract fun providesBoardRepository(board: FakeBoardRepository): BoardRepository

    @Module
    companion object {

        @Provides
        internal fun providesAppDatabase(application: Application): AppDatabase {
            return Room.inMemoryDatabaseBuilder(
                application,
                AppDatabase::class.java
            ).allowMainThreadQueries()
                .build()

        }

        @Provides
        internal fun providesBoardDao(database: AppDatabase): BoardDao {
            return database.boardDao
        }
    }
}