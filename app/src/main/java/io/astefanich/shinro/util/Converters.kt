package io.astefanich.shinro.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.Grid
import io.astefanich.shinro.model.Blacklist
import timber.log.Timber

object Converters {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun gridToJson(grid: Grid): String = gson.toJson(grid)

    @TypeConverter
    @JvmStatic
    fun jsonToGrid(str: String): Grid = gson.fromJson(str, Grid::class.java)

    @TypeConverter
    @JvmStatic
    fun difficultyToString(diff: Difficulty): String = diff.name

    @TypeConverter
    @JvmStatic
    fun stringToDifficulty(str: String): Difficulty = Difficulty.valueOf(str)

    @TypeConverter
    @JvmStatic
    fun queueToString(q: Blacklist): String {
        val sb = StringBuilder()
        val ids = q.toIntArray().forEach { sb.append("$it ") }
        return sb.toString()
    }

    @TypeConverter
    @JvmStatic
    fun stringToQueue(str: String): Blacklist {
        Timber.i("str -> blacklist from $str")
        val queue = Blacklist()
        if (str.trim() == ""){
            Timber.i("its an empty queue")
            return queue
        }

        val tokens = str.trim().split(" ")
        for(i in 0 until tokens.size){
            Timber.i("da string is ${tokens[i]}")
            queue.add(Integer.parseInt(tokens[i]))
        }
//        str.split(" ").forEach { s->
//            Timber.i("adding $s to queue")
//            queue.add(Integer.parseInt(s))
//        }
        return queue
    }
}