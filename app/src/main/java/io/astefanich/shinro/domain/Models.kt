package io.astefanich.shinro.domain

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

//abstract class Cell(var current: Char, val actual: Char)
//
//data class Arrow(val direction: Char) : Cell(direction, direction)
//data class Number(val number: Char) : Cell(number, number)
//data class MCell(val original: Char) : Cell('O', original)

data class Cell(var current: Char, val actual: Char = current)

@Entity(tableName = "board_table")
data class Board(


    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "board_id")
    val boardId: Int,

    val difficulty: String,

//    @Embedded
//    val cells: List<List<Cell>>,

    var completed: Boolean = false,

    @ColumnInfo(name = "marbles_placed")
    var marblesPlaced: Int = 0

)


data class Instruction(
    val stepNum: Int,
    val image: String,
    val text: String
)

