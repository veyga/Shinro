package io.astefanich.shinro.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "board_table")
data class Board(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "board_id")
    val boardId: Int,

    val difficulty: String,

    val grid: Grid,

    var completed: Boolean = false,

    @ColumnInfo(name = "marbles_placed")
    var marblesPlaced: Int = 0

)

data class Cell(var current: String, val actual: String = current)

data class Grid(val cells: Array<Array<Cell>>)

data class Move(val row: Int, val column: Int, val oldVal: String, val newVal: String)

data class Instruction(val drawable: Int, val text: String)

@Parcelize
enum class InstructionType : Parcelable { GENERAL, PATHFINDER, BLOCKER, PIGEONHOLE }

data class DatabaseName(val name: String)

