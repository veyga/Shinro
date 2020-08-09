package io.astefanich.shinro.di

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.R
import io.astefanich.shinro.database.AppDatabase
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.database.BoardGenerator
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.BoardCount
import io.astefanich.shinro.domain.DatabaseName
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Named


@Module
class AppModule {


    @PerApplication
    @Provides
    internal fun providesContext(application: Application): Context = application.applicationContext


    @PerApplication
    @Provides
    internal fun providesVideoURI(): Uri =
        Uri.parse("android.resource://io.astefanich.shinro/" + R.raw.what_is_shinro)


    @PerApplication
    @Provides
    fun providesToaster(ctx: Context): (String) -> Unit {
        return { msg: String -> Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show() }
    }

    @PerApplication
    @Provides
    @Named("lastVisitedFile")
    fun providesLastVisitedFileName(): String = "last_visited.txt"


    @PerApplication
    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao = database.boardDao()


    @PerApplication
    @Provides
    internal fun providesDatabaseName(ct: BoardCount): DatabaseName =
        DatabaseName("shinro${ct.value}.db")


    @PerApplication
    @Provides
    internal fun providesDatabaseFromFile(
        application: Application,
        databaseName: DatabaseName
    ): AppDatabase {
        Timber.i("creating db")
        return Room.databaseBuilder(application, AppDatabase::class.java, databaseName.name)
            .allowMainThreadQueries()
            .createFromAsset(databaseName.name)
            .build()
    }

    @PerApplication
    @Provides
    internal fun providesBoardCount(): BoardCount = BoardCount(2)


    /*
        The below are for testing/board creation.
        Releases should output to DB file, and app should load from file
     */
    @PerApplication
//    @Provides
    internal fun providesBoardGenerator(boardCount: BoardCount): BoardGenerator =
        BoardGenerator(boardCount)

    @PerApplication
//    @Provides
    internal fun providesBoards(generator: BoardGenerator): Array<Board?> = generator.genBoards()

    @PerApplication
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


