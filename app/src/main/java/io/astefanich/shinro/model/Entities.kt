package io.astefanich.shinro.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.astefanich.shinro.domain.Difficulty
import io.astefanich.shinro.domain.Grid
import java.util.*

@Entity(tableName = "game_table")
data class Game(

    @PrimaryKey(autoGenerate = false)
    val id: Int = 1, //only storing 1 game (the active game) in the db

    val difficulty: Difficulty,

    val board: Grid,

    @ColumnInfo(name = "marbles_placed")
    var marblesPlaced: Int = 0,

    @ColumnInfo(name = "time_elapsed")
    var timeElapsed: Long = 0,

    @ColumnInfo(name = "freebies_remaining")
    var freebiesRemaining: Int = 1
)

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

@Entity(tableName = "blacklist_table")
data class Blacklist(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val difficulty: Difficulty,

    val reserved: Queue<Int>
)
