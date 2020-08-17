package io.astefanich.shinro.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Game
import io.astefanich.shinro.util.Converters

@Database(entities = [Board::class, Game::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun boardDao(): BoardDao

    abstract fun gameDao(): GameDao

}
