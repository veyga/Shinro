package io.astefanich.shinro.domain


enum class Difficulty { EASY, MEDIUM, HARD }

//enum class CompletionStatus { INCOMPLETE, COMPLETE }

data class Cell(var current: Char, val actual: Char)

data class Board(
    val boardNum: Int,
    val difficulty: Difficulty,
    val cells: Array<Array<Cell>>,
    var completed: Boolean = false,
    var marblesRemaining: Int = 10
)