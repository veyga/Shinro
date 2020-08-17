package io.astefanich.shinro.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//enum class Difficulty(val repr: String) {
//    EASY("EASY"),
//    MEDIUM("MEDIUM"),
//    HARD("HARD")
//}
enum class Difficulty { EASY, MEDIUM, HARD }


//val DIFFICULTIES: Array<String> = arrayOf(Difficulty.)

@Entity(tableName = "board_table")
data class Board(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "board_num")
    val boardNum: Int,  //multiple difficulties share puzzle nums

    val difficulty: Difficulty,

    val cells: Grid
)

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

//// between gameVM and gameOver
@Parcelize
data class GameSummary(val difficulty: Difficulty, val win: Boolean, val time: Long) : Parcelable


sealed class PlayRequest : Parcelable {
    @Parcelize
    object Resume : PlayRequest()

    @Parcelize
    data class NewGame(val difficulty: Difficulty) : PlayRequest()
}

@Parcelize
enum class TipChoice : Parcelable { HOWTOPLAY, PATHFINDER, BLOCKER, PIGEONHOLE }

data class Tip(val drawable: Int, val text: String)

data class Cell(var current: String, val actual: String = current)

typealias Grid = Array<Array<Cell>>

data class DatabaseName(val name: String)

