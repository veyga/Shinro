package io.astefanich.shinro.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Cell(var current: Char, val actual: Char)

@Entity(tableName = "board_table")
data class Board(


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "board_id")
    val boardId: Int,

    val difficulty: String,

    var completed: Boolean = false,

    @ColumnInfo(name = "marbles_placed")
    var marblesPlaced: Int = 0
)



