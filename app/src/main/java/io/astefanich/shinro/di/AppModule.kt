package io.astefanich.shinro.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.database.AppDatabase
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.repository.BoardRepository
import io.astefanich.shinro.repository.BoardRepositoryImpl
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    internal fun providesAppDatabase(application: Application): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            application,
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()

    }

    @Singleton
    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao {
        return database.boardDao
    }

    @Singleton
    @Provides
    internal fun providesBoardRepository(dao: BoardDao): BoardRepository {
        return BoardRepositoryImpl(dao)
    }
}

