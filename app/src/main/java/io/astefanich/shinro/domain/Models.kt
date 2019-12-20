package io.astefanich.shinro.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Cell(var current: Char, val actual: Char)

@Entity(tableName = "board_table")
data class Board(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "board_id")
    val boardNum: Int,

    val difficulty: String,

    var completed: Boolean = false,

    @ColumnInfo(name = "marbles_remaining")
    var marblesRemaining: Int = 10
)

