package io.astefanich.shinro.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.database.*
import io.astefanich.shinro.model.Board
import io.astefanich.shinro.model.ResultAggregate
import io.astefanich.shinro.util.EventBusIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Named


@Module
class AppModule {

    @PerApplication
    @Provides
    @Named("appCtx")
    internal fun providesAppContext(application: Application): Context =
        application.applicationContext

    @PerApplication
    @Provides
    fun providesSharesPreferences(@Named("appCtx") ctx: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(ctx)

    @PerApplication
    @Provides
    fun providesEventBus(): EventBus {
        EventBus.builder().addIndex(EventBusIndex()).installDefaultEventBus()
        return EventBus.getDefault()
    }

    @Provides
    internal fun providesGameDao(database: AppDatabase): GameDao = database.gameDao()

    @Provides
    internal fun providesResultsDao(database: AppDatabase): ResultsDao = database.resultsDao()

    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao = database.boardDao()

    @Provides
    internal fun providesDatabaseName(): DatabaseName = DatabaseName("shinroinitial.db")


    @PerApplication
    @Provides
    internal fun providesDatabaseFromFile(
        application: Application,
        databaseName: DatabaseName
    ): AppDatabase {
        Timber.i("creating db")
        return Room.databaseBuilder(application, AppDatabase::class.java, databaseName.name)
            .fallbackToDestructiveMigration()
            .createFromAsset(databaseName.name)
            .build()
    }


    /*
        The below are for testing/board creation.
        Releases should output to DB file, and app should load from file
     */

    //    @PerApplication
//    @Provides
    fun providesDatabaseFromFileWithCallback(
        application: Application,
        databaseName: DatabaseName,
        boards: Array<Board>,
        seeds: Array<ResultAggregate>,
    ): AppDatabase {
        Timber.i("creating db with cb")
        lateinit var appDatabase: AppDatabase
        appDatabase = Room.databaseBuilder(application, AppDatabase::class.java, databaseName.name)
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    GlobalScope.launch(Dispatchers.IO) {
                        //race condition
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

    //    @PerApplication
//    @Provides
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
                        //race condition
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

    //    @PerApplication
//    @Provides
    internal fun providesBoards(): Array<Board?> = BoardGenerator().genBoards()

    //    @PerApplication
//    @Provides
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


