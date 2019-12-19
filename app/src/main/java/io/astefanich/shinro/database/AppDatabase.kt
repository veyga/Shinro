package io.astefanich.shinro.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.astefanich.shinro.domain.Board

@Database(entities = [Board::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

//    val DATABASE_NAME = "app_db"
    abstract val boardDao: BoardDao
}
