package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.database.ResultsDao
import io.astefanich.shinro.model.GameResult
import javax.inject.Inject

class StatisticsRepository
@Inject
constructor(
    val statsDao: ResultsDao
) {

//    @Entity(tableName = "results_table")
//    data class GameResult(
//
//        @PrimaryKey(autoGenerate = true)
//        val id: Long,
//
//        val difficulty: Difficulty,
//
//        val win: Boolean,
//
//        val time: Long,
//
//        val points: Int
//    )
//    private val stats= MutableLiveData<GameResult>()
}