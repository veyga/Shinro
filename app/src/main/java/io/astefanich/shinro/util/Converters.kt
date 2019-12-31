package io.astefanich.shinro.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.astefanich.shinro.domain.Grid

object Converters {


    @TypeConverter
    @JvmStatic
    fun gridToJson(value: Grid): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToGrid(value: String): Grid {
        return Gson().fromJson(value, Grid::class.java)
    }

}