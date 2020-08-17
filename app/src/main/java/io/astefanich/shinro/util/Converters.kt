package io.astefanich.shinro.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.astefanich.shinro.domain.Difficulty
import io.astefanich.shinro.domain.Grid

object Converters {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun gridToJson(grid: Grid): String {
        return gson.toJson(grid)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToGrid(str: String): Grid {
        return gson.fromJson(str, Grid::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun difficultyToString(diff: Difficulty): String = diff.name

    @TypeConverter
    @JvmStatic
    fun stringToDifficulty(str: String): Difficulty = Difficulty.valueOf(str)
}