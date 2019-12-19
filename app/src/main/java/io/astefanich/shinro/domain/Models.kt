package io.astefanich.shinro.domain


enum class Difficulty { EASY, MEDIUM, HARD }


data class Cell(var current: Char, val actual: Char)

data class Board(
    val boardNum: Int,
    val difficulty: Difficulty,
    var completed: Boolean = false,
    var marblesRemaining: Int = 10
)