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
class AppModule {

    @Singleton
    @Provides
    internal fun providesBoardRepository(): BoardRepository {
        return FakeBoardRepository(Triple("EASY", "MEDIUM", "HARD"))
    }

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
}

//@Module
//abstract class AppModule {
//
////    @Singleton
////    @Binds
////    internal abstract fun providesBoardRepository(repository: FakeBoardRepository): BoardRepository
//
//    @Module
//    companion object {
//
//        @Singleton
//        @Provides
//        internal fun providesAppDatabase(application: Application): AppDatabase {
//            return Room.inMemoryDatabaseBuilder(
//                application,
//                AppDatabase::class.java
//            ).allowMainThreadQueries()
//                .build()
//
//        }
//
//        @Singleton
//        @Provides
//        internal fun providesBoardDao(database: AppDatabase): BoardDao {
//            return database.boardDao
//        }
//
//        @Singleton
//        @Provides
//        internal fun providesBoardRepository(): BoardRepository {
//            return FakeBoardRepository(Triple("EASY", "MEDIUM", "HARD"))
//        }
//    }
//}
