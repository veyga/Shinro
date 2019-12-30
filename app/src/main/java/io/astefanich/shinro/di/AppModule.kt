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
import io.astefanich.shinro.domain.Cell
import io.astefanich.shinro.domain.Instruction
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
    internal fun providesSampleBoards(): Array<Board> {
//        val board1 = Board(
//            1, "EASY",
//            listOf(
//                //row 0
//                listOf(
//                    Cell('0'),
//                    Cell('1'),
//                    Cell('2'),
//                    Cell('1'),
//                    Cell('1'),
//                    Cell('1'),
//                    Cell('3'),
//                    Cell('2'),
//                    Cell('1')
//                )
//            )
//        )

//        return arrayOf(board1)
        return arrayOf(Board(1,"easy"))
    }


    @Singleton
    @Provides
    internal fun providesInstructions(): List<Instruction> {
        return arrayListOf(
            Instruction(1, "IMG1", "step1"),
            Instruction(2, "IMG2", "step2"),
            Instruction(3, "IMG3", "step3"),
            Instruction(4, "IMG4", "step4"),
            Instruction(5, "IMG5", "step5"),
            Instruction(6, "IMG6", "step6"),
            Instruction(7, "IMG7", "step7"),
            Instruction(8, "IMG8", "step8"),
            Instruction(9, "IMG9", "step9")
        )
    }
}

