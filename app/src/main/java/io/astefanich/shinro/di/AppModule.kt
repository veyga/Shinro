package io.astefanich.shinro.di

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.R
import io.astefanich.shinro.database.AppDatabase
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Cell
import io.astefanich.shinro.domain.Grid
import io.astefanich.shinro.repository.BoardRepository
import io.astefanich.shinro.repository.BoardRepositoryImpl
import io.astefanich.shinro.repository.FakeBoardRepository
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    internal fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    internal fun providesInMemoryAppDatabase(
        application: Application, boards: Array<Board>
    ): AppDatabase {

        lateinit var appDatabase: AppDatabase
        appDatabase = Room.inMemoryDatabaseBuilder(
            application, AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Executors.newSingleThreadScheduledExecutor().execute() {
                        Timber.i("ONCREATE")
                        appDatabase.boardDao().insertBoards(*boards)
                        Timber.i("BOARDS LOADED")
                    }
                }
            }
            )
            .build()
        return appDatabase
    }


    @Singleton
    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao = database.boardDao()

    @Singleton
//    @Provides
    internal fun providesFakeBoardRepository(boards: Array<Board>): BoardRepository =
        FakeBoardRepository(boards)

    @Singleton
    @Provides
    internal fun providesBoardRepositoryImpl(dao: BoardDao): BoardRepository =
        BoardRepositoryImpl(dao)

    @Singleton
    @Provides
    internal fun providesSampleBoards(): Array<Board> {
        val board1 =
            """
        1
        EASY
        0 1 2 1 1 1 3 2 1
        2 M X C E E M X X
        0 X X X X B X X X
        3 X M H X M E M G
        1 X X X B X X X M
        0 X X X X X X X X
        3 C E X M A M M G
        1 X M X F D X X X
        2 X X M X A M X A 
        """.trimIndent()

        val board2 =
            """
        2
        EASY
        0 3 1 2 1 1 1 2 1
        2 M X X M X X X X
        2 M X B X X E M X
        1 C X M X X X D X
        1 X X X C X X X M
        1 M X X X X G X X
        2 C X M X X M X X
        2 C M A X X F M X
        1 X X X X M X A X
        """.trimIndent()

        fun boardFromString(str: String): Board {
            val lines = str.lines()
            val boardId = lines[0].toInt()
            val difficulty = lines[1]
            val cells = Array(9) { Array(9) { Cell(" ") } }
            for (i in 0..8) {
                val chars = lines[i + 2].split(" ")
                for (j in 0..8) {
                    val actual = chars[j]
                    if (actual == "M" || actual == "X")
                        cells[i][j] = Cell(" ", actual)
                    else
                        cells[i][j] = Cell(actual)
                }
            }
            return Board(boardId, difficulty, Grid(cells))

        }

        return arrayOf(boardFromString(board1), boardFromString(board2))
    }

    @Singleton
    @Provides
    internal fun providesVideoURI(): Uri {
        return Uri.parse("android.resource://io.astefanich.shinro/" + R.raw.what_is_shinro)
    }

}

