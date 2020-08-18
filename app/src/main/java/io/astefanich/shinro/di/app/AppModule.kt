package io.astefanich.shinro.di.app

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.database.*
import io.astefanich.shinro.di.PerApplication
import io.astefanich.shinro.model.Board
import io.astefanich.shinro.model.Game
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.Executors


@Module
class AppModule {

    @PerApplication
    @Provides
    internal fun providesContext(application: Application): Context = application.applicationContext

    //    @PerApplication
//    @Provides
//    fun providesToaster(ctx: Context): (String) -> Unit {
//        return { msg: String -> Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show() }
//    }

    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao = database.boardDao()

    @Provides
    internal fun providesGameDao(database: AppDatabase): GameDao = database.gameDao()

    @Provides
    internal fun providesResultsDao(database: AppDatabase): ResultsDao = database.resultsDao()

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
    internal fun providesInitialGame(board: Board): Game = Game(difficulty = board.difficulty, board = board.cells)

    @PerApplication
    @Provides
    internal fun providesInMemoryAppDatabase(
        application: Application,
        boards: Array<Board>,
        initialGame: Game
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
                        Timber.i("BOARDS INSERTED")
                        appDatabase.boardDao().insertBoards(*boards)
                    }
                }
            })
            .build()
        return appDatabase
    }
}


