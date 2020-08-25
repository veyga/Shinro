package io.astefanich.shinro.di.app

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.database.*
import io.astefanich.shinro.di.PerApplication
import io.astefanich.shinro.model.Board
import io.astefanich.shinro.model.ResultAggregate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Named

@Module
class DBModule {

    @Provides
    internal fun providesGameDao(@Named("production") database: AppDatabase): GameDao =
        database.gameDao()

    @Provides
    internal fun providesResultsDao(@Named("production") database: AppDatabase): ResultsDao =
        database.resultsDao()

    @Provides
    internal fun providesBoardDao(@Named("production") database: AppDatabase): BoardDao =
        database.boardDao()

    @Provides
    internal fun providesDatabaseName(): DatabaseName = DatabaseName("shinroinitial.db")


    @PerApplication
    @Provides
    @Named("production")
    internal fun providesDatabaseFromFile(
        application: Application,
        databaseName: DatabaseName
    ): AppDatabase {
        Timber.i("creating from production DB")
        return Room.databaseBuilder(application, AppDatabase::class.java, databaseName.name)
            .fallbackToDestructiveMigration()
            .createFromAsset(databaseName.name)
            .build()
    }


/*
    The below are for testing/board creation.
    Releases should output to DB file, and app should load from file.
    Using the below DBs can lead to race conditions on initialization.
    Implement delays accordingly in appropriate coroutines
 */

    @PerApplication
    @Provides
    @Named("fileOverwrite")
    fun providesDatabaseFromFileWithFullInitialization(
        application: Application,
        databaseName: DatabaseName,
        boards: Array<Board>,
        seeds: Array<ResultAggregate>,
    ): AppDatabase {
        Timber.i("creating db to file with full initialization")
        lateinit var appDatabase: AppDatabase
        appDatabase = Room.databaseBuilder(application, AppDatabase::class.java, databaseName.name)
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    GlobalScope.launch(Dispatchers.IO) {
                        appDatabase.resultsDao().insertAggregates(*seeds)
                        Timber.i("AGGREGATE SEEDS INSERTED")
                        appDatabase.boardDao().insertBoards(*boards)
                        Timber.i("BOARDS INSERTED")
                    }
                }
            })
            .build()
        return appDatabase
    }

    @PerApplication
    @Provides
    @Named("inMemory")
    internal fun providesInMemoryAppDatabase(
        application: Application,
        boards: Array<Board>,
        seeds: Array<ResultAggregate>,
    ): AppDatabase {
        lateinit var appDatabase: AppDatabase
        appDatabase = Room.inMemoryDatabaseBuilder(application, AppDatabase::class.java)
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    GlobalScope.launch(Dispatchers.IO) {
                        appDatabase.resultsDao().insertAggregates(*seeds)
                        Timber.i("AGGREGATE SEEDS INSERTED")
                        appDatabase.boardDao().insertBoards(*boards)
                        Timber.i("BOARDS INSERTED")
                    }
                }
            })
            .build()
        return appDatabase
    }

    @Provides
    fun providesAggregateSeeds(): Array<ResultAggregate> {
        val gen = { diff: Difficulty ->
            ResultAggregate(
                difficulty = diff,
                numPlayed = 0,
                numWins = 0,
                bestTimeSec = Long.MAX_VALUE,
                totalTimeSeconds = 0L
            )
        }
        return arrayOf(
            gen(Difficulty.EASY),
            gen(Difficulty.MEDIUM),
            gen(Difficulty.HARD),
            gen(Difficulty.ANY),
        )
    }
}
