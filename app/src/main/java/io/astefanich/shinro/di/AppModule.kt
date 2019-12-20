package io.astefanich.shinro.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.database.AppDatabase
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import io.astefanich.shinro.repository.BoardRepositoryImpl
import io.astefanich.shinro.repository.FakeBoardRepository
import timber.log.Timber
import javax.inject.Singleton

@Module
class AppModule {


    @Singleton
    @Provides
    internal fun providesAppDatabase(application: Application, boards: Array<Board>): AppDatabase {

        lateinit var appDatabase: AppDatabase
        appDatabase = Room.inMemoryDatabaseBuilder(
            application,
            AppDatabase::class.java
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    appDatabase.boardDao().insertBoards(*boards)
                    Timber.i("ONCREATE") //not getting logged
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Timber.i("ONOPEN") //not getting logged
                }
            }
            )
            .build()
        Timber.i("database is open? ${appDatabase.isOpen}") //logging false
        return appDatabase
    }

    @Singleton
    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao {
        return database.boardDao()
    }

    @Singleton
//    @Provides
    internal fun providesFakeBoardRepository(boards: Array<Board>): BoardRepository {
        return FakeBoardRepository(boards)
    }

    @Singleton
    @Provides
    internal fun providesBoardRepositoryImpl(dao: BoardDao): BoardRepository {
        return BoardRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    internal fun providesSampleData(): Array<Board> {
        return arrayOf(
            Board(1, "easy"),
            Board(2, "medium"),
            Board(3, "hard")
        )
    }
}

