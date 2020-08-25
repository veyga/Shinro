package io.astefanich.shinro.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.astefanich.shinro.model.*

data class DatabaseName(val name: String)

@Database(
    entities = [
        Game::class,
        Board::class,
        ResultAggregate::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun boardDao(): BoardDao

    abstract fun gameDao(): GameDao

    abstract fun resultsDao(): ResultsDao
}
