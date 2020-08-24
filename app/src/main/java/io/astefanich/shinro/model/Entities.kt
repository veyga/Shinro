package io.astefanich.shinro.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.Freebie
import io.astefanich.shinro.common.Grid

@Entity(tableName = "game_table")
data class Game(

    @PrimaryKey(autoGenerate = false)
    val id: Int = 1, //only storing 1 game (the active game) in the db

    val difficulty: Difficulty,

    var board: Grid,

    @ColumnInfo(name = "marbles_placed")
    var marblesPlaced: Int = 0,

    @ColumnInfo(name = "is_complete")
    var isComplete: Boolean = false,

    @ColumnInfo(name= "is_win")
    var isWin: Boolean = false,

    @ColumnInfo(name = "time_elapsed")
    var timeElapsed: Long = 0,

    @ColumnInfo(name = "freebie")
    var freebie: Freebie = Freebie(0,0)
){
    //0,0 -> freebie available
    fun freebiesRemaining(): Int = if (freebie == Freebie(0, 0)) 1 else 0
}

@Entity(tableName = "results_table")
data class GameResult(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val difficulty: Difficulty,

    val win: Boolean,

    val time: Long,

    val points: Int
)

@Entity(tableName = "board_table")
data class Board(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "board_num")
    val boardNum: Int,  //multiple difficulties share puzzle nums

    val difficulty: Difficulty,

    val cells: Grid
)

