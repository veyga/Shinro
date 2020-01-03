package io.astefanich.shinro.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.astefanich.shinro.domain.Grid

object Converters {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun gridToJson(value: Grid): String {
        return gson.toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToGrid(value: String): Grid {
        return gson.fromJson(value, Grid::class.java)
    }

}