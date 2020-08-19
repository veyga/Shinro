package io.astefanich.shinro.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.database.*
import io.astefanich.shinro.model.Blacklist
import io.astefanich.shinro.model.Board
import io.astefanich.shinro.model.BoardHistory
import io.astefanich.shinro.model.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Named


@Module
class AppModule {

    @PerApplication
    @Provides
    @Named("appCtx")
    internal fun providesAppContext(application: Application): Context = application.applicationContext

//    @Provides
//    fun providesAppComponent(application: Application): AppComponent {
//        Timber.i("providing new app component")
//        return AppComponent.builder().application(application).build()
//    }

    @Provides
    internal fun providesGameDao(database: AppDatabase): GameDao = database.gameDao()

    @Provides
    internal fun providesResultsDao(database: AppDatabase): ResultsDao = database.resultsDao()

    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao = database.boardDao()

    @Provides
    internal fun providesBlacklistDao(database: AppDatabase): BoardHistoryDao =
        database.boardHistoryDao()

    //    @PerApplication
//    @Provides
    internal fun providesDatabaseName(): DatabaseName = DatabaseName("shinro.db")


    //    @PerApplication
//    @Provides
    internal fun providesDatabaseFromFile(
        application: Application,
        databaseName: DatabaseName
    ): AppDatabase {
        Timber.i("creating db")
        return Room.databaseBuilder(application, AppDatabase::class.java, databaseName.name)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset(databaseName.name)
            .build()
    }


    /*
        The below are for testing/board creation.
        Releases should output to DB file, and app should load from file
     */
    @PerApplication
    @Provides
    internal fun providesBoards(): Array<Board?> = BoardGenerator().genBoards()

    @PerApplication
    @Provides
    internal fun providesInitialBoard(boards: Array<Board>): Board = boards[0]

    @PerApplication
    @Provides
    internal fun providesInitialGame(board: Board): Game =
        Game(difficulty = board.difficulty, board = board.cells)

    @PerApplication
    @Provides
    internal fun providesInitialBlacklists(): List<BoardHistory> =
        listOf(
            BoardHistory(difficulty = Difficulty.EASY, blacklist = Blacklist()),
            BoardHistory(difficulty = Difficulty.MEDIUM, blacklist = Blacklist()),
            BoardHistory(difficulty = Difficulty.HARD, blacklist = Blacklist())
        )

    @PerApplication
    @Provides
    internal fun providesInMemoryAppDatabase(
        application: Application,
        boards: Array<Board>,
        blacklists: List<BoardHistory>
    ): AppDatabase {
        lateinit var appDatabase: AppDatabase
        appDatabase = Room.inMemoryDatabaseBuilder(
            application, AppDatabase::class.java
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    GlobalScope.launch(Dispatchers.IO) {
                        //race condition
                        blacklists.forEach { appDatabase.boardHistoryDao().insertBoardHistory(it) }
                        Timber.i("BLACKLISTS INSERTED")
                        appDatabase.boardDao().insertBoards(*boards)
                        Timber.i("BOARDS INSERTED")
                    }
                }
            })
            .build()
        return appDatabase
    }
}


