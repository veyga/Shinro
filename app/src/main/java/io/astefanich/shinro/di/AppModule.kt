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
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
//    @Provides
    internal fun providesAppDatabaseFromFile(application: Application): AppDatabase {
        Timber.i("providing db from file")
        return Room.databaseBuilder(application, AppDatabase::class.java, "ShinroDB")
            .createFromAsset("shinro.db")
            .addCallback(object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Timber.i("ONCREATE") //not getting logged
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Timber.i("ONOPEN") //not getting logged
                }
            })
            .build()

    }

    @Singleton
    @Provides
    internal fun providesInMemoryAppDatabase(
        application: Application,
        boards: Array<Board>
    ): AppDatabase {

        Timber.i("providing in memory db")
        lateinit var appDatabase: AppDatabase
        appDatabase = Room.inMemoryDatabaseBuilder(
            application, AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Timber.i("ONCREATE in main thread") //not getting logged
                    Executors.newSingleThreadScheduledExecutor().execute() {
                        Timber.i("ONCREATE in executor") //not getting logged
                        appDatabase.boardDao().insertBoards(*boards)
                        Timber.i("database is open? ${appDatabase.isOpen}") //logging false
                        Timber.i("loaded data?")
                    }
                }
            }
            )
            .build()
        return appDatabase
    }

    @Singleton
    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao {
        Timber.i("providing boardDao")
        return database.boardDao()
    }

    @Singleton
//    @Provides
    internal fun providesFakeBoardRepository(boards: Array<Board>): BoardRepository {
        Timber.i("fake repo provided")
        return FakeBoardRepository(boards)
    }

    @Singleton
    @Provides
    internal fun providesBoardRepositoryImpl(dao: BoardDao): BoardRepository {
        Timber.i("real repo provided")
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

