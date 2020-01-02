package io.astefanich.shinro.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Cell(var current: String, val actual: String = current)

data class Grid(val cells: Array<Array<Cell>>)

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

data class Move(val row: Int, val column: Int, val oldVal: String, val newVal: String)


data class Instruction(
    val type: InstructionType,
    val drawable: Int,
    val stepNum: Int,
    val text: String
)

enum class InstructionType { GENERAL, PATHFINDER, BLOCKER, PIGEONHOLE}

