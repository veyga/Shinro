package io.astefanich.shinro.di

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.R
import io.astefanich.shinro.database.AppDatabase
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.DatabaseName

@Module
class AppModule {

    @AppScope
    @Provides
    internal fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @AppScope
    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao = database.boardDao()

    @AppScope
    @Provides
    internal fun providesVideoURI(): Uri =
        Uri.parse("android.resource://io.astefanich.shinro/" + R.raw.what_is_shinro)

    @AppScope
    @Provides
    internal fun providesDatabaseName(): DatabaseName = DatabaseName("shinro.db")

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

}


