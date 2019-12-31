package io.astefanich.shinro.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.util.Converters

@Database(entities = [Board::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    //    val DATABASE_NAME = "app_db"
    abstract fun boardDao(): BoardDao

}
