package io.astefanich.shinro.di

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.R
import io.astefanich.shinro.database.AppDatabase
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.database.BoardGenerator
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.BoardCount
import io.astefanich.shinro.domain.DatabaseName
import io.astefanich.shinro.domain.Progress
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Named


@Module
class AppModule {

    @AppScope
    @Provides
    internal fun providesProgress(dao: BoardDao): List<Progress> = dao.getProgress()

    @AppScope
    @Provides
    internal fun providesContext(application: Application): Context = application.applicationContext

    @AppScope
    @Provides
    internal fun providesVideoURI(): Uri =
        Uri.parse("android.resource://io.astefanich.shinro/" + R.raw.what_is_shinro)


    @AppScope
    @Provides
    fun providesLogger(ctx: Context): (String) -> Unit {
        return { msg: String -> Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show() }
//        return { msg: String -> Timber.i("Your message is... $msg") }
    }

    @AppScope
    @Provides
    @Named("winBuzz")
    fun providesWinBuzzPattern(): LongArray = longArrayOf(0, 500)

    @AppScope
    @Provides
    @Named("resetBuzz")
    fun providesResetBuzzPattern(): LongArray = longArrayOf(0, 50)

    @AppScope
    @Provides
    @Named("lastVisitedFile")
    fun providesLastVisitedFileName(): String = "last_visited.txt"


    @AppScope
    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao = database.boardDao()


    @AppScope
    @Provides
    internal fun providesDatabaseName(ct: BoardCount): DatabaseName =
        DatabaseName("shinro${ct.value}.db")

    @AppScope
    @Provides
    internal fun providesDatabaseFromFile(
        application: Application,
        databaseName: DatabaseName
    ): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, databaseName.name)
            .allowMainThreadQueries()
            .createFromAsset(databaseName.name)
            .build()
    }

    @AppScope
    @Provides
    internal fun providesBoardCount(): BoardCount = BoardCount(50)


    /*
        The below are for testing/board creation.
        Releases should output to DB file, and app should load from file
     */
    @AppScope
//    @Provides
    internal fun providesBoardGenerator(boardCount: BoardCount): BoardGenerator =
        BoardGenerator(boardCount)

    @AppScope
//    @Provides
    internal fun providesBoards(generator: BoardGenerator): Array<Board?> = generator.genBoards()

    @AppScope
//    @Provides
    internal fun providesInMemoryAppDatabase(
        application: Application,
        boards: Array<Board>
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
                        Timber.i("ONCREATE INMEMORY")
                        appDatabase.boardDao().insertBoards(*boards)
                        Timber.i("BOARDS LOADED INMEMORY")
                    }
                }
            })
            .build()
        return appDatabase
    }

}


