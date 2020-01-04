package io.astefanich.shinro.database

import dagger.Provides
import io.astefanich.shinro.di.AppScope
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Cell
import io.astefanich.shinro.domain.Grid

object BoardGenerator {

    val stringz = arrayOf(
        """
        1
        EASY
        0 1 2 1 1 1 3 2 1
        2 M X C E E M X X
        0 X X X X B X X X
        3 X M H X M E M G
        1 X X X B X X X M
        0 X X X X X X X X
        3 C E X M A M M G
        1 X M X F D X X X
        2 X X M X A M X A 
        """.trimIndent(),
        """
        2
        EASY
        0 3 1 2 1 1 1 2 1
        2 M X X M X X X X
        2 M X B X X E M X
        1 C X M X X X D X
        1 X X X C X X X M
        1 M X X X X G X X
        2 C X M X X M X X
        2 C M A X X F M X
        1 X X X X M X A X
        """.trimIndent(),
        """
        3
        EASY
        0 2 0 2 1 5 1 0 1
        1 M X F F D X X X
        1 X X M X X X X X
        4 M X M C M M X X
        1 X X A C X X X M
        2 X X X M M X H X
        1 X X X X M A H A
        1 X B X X M X H X
        1 X X X X M X X X
        """.trimIndent(),
        """
            4
            EASY
            0 0 2 3 1 1 3 2 0
            1 C D M X X X X X
            1 D D X X X M X X
            0 D X A D X X E X
            2 C X M X M X F X
            0 X B B X X X X X
            1 X M G X H E X X
            5 X M M M X M M X
            2 X X X X X M M X
            """.trimIndent(),
        """
            5
            EASY
            0 1 0 1 2 1 1 1 5
            3 X X X E X M M M
            4 M X X M M G H M
            1 X X X B X X X M
            0 X B X X X D X X
            0 D X X X F A X A
            1 X X X X X F A M
            2 X X M X X X X M
            1 X X X M X X X X
        """.trimIndent(),
        """
            6
            EASY
            0 3 1 3 0 1 1 1 2
            1 X X M X X X X X
            4 M B M X M X X M
            0 E X X X X X X X
            1 X D X X X X M A
            1 A M F X X B X X
            1 M X X D X X A G
            3 M X M H X X F M
            1 X X X X X M H X
             """.trimIndent(),
        """
           7
           EASY
           0 5 1 2 0 0 0 1 3
           4 M M M X X X X M
           1 M D H D X X X X
           1 A X F X X X X M
           1 X X M X X X X X
           2 M B X X X G M A
           1 M X A X X X X X
           0 X X X X X X X X
           2 M X C X G X X M
       """.trimIndent(),
        """
        8
        EASY
        0 2 3 3 1 0 0 1 2
        3 E M M F X X X M
        1 X M X F X X X X
        1 X M H D X X X X
        1 D X X M X X X X
        2 M X B X G X X M
        2 X X M X X X M A
        0 X X X X X X X X
        2 M A M X X X X X
    """.trimIndent(),
        """
            9
            EASY
            0 1 1 3 1 0 3 2 1
            2 X C M X X M X X
            2 X X M C X X M X
            2 X C C M X F E M
            0 E X X X X X X X
            2 X X M X X M X X
            1 M X B X X E X H
            2 A M X X X X M X
            1 B X X X X M X H
        """.trimIndent(),
        """
            10
            EASY
            0 1 3 2 3 1 1 1 0
            2 X M G M X X X F
            1 X M F X X X X X
            1 X X M E F X X X
            3 M B D M M X X F
            3 X X M M G X M G
            0 X X X X X X H X
            0 X X F X X X X X
            2 X M X X C M X X
        """.trimIndent()

    )

    private fun boardFromString(str: String): Board {
        val lines = str.lines()
        val boardId = lines[0].toInt()
        val difficulty = lines[1]
        val cells = Array(9) { Array(9) { Cell(" ") } }
        for (i in 0..8) {
            val chars = lines[i + 2].split(" ")
            for (j in 0..8) {
                val actual = chars[j]
                if (actual == "M" || actual == "X")
                    cells[i][j] = Cell(" ", actual)
                else
                    cells[i][j] = Cell(actual)
            }
        }
        return Board(boardId, difficulty, Grid(cells))

    }


    fun getBoards(): Array<Board?> {
        val boards = arrayOfNulls<Board>(10)

        for (i in stringz.indices)
            boards[i] = boardFromString(stringz[i])

        return boards
    }
}