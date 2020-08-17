package io.astefanich.shinro.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.astefanich.shinro.domain.Difficulty
import io.astefanich.shinro.domain.Grid
import timber.log.Timber

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
    fun difficultyToString(diff: Difficulty): String {
        Timber.i("converting difficulty $diff to string")
        return diff.repr
    }

    @TypeConverter
    @JvmStatic
    fun stringToDifficulty(str: String): Difficulty {
        Timber.i("converting string $str to difficulty")
        return Difficulty.valueOf(str)
    }
}