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
        0 2 1 0 2 3 2 0 2
        3 E M D X M M X X
        3 M X G M X C X M
        1 X X X X X M X X
        0 X D X X B X X X
        0 X X X X X X X A
        2 X X C M M A X X
        2 M X G X X C X M
        1 X B X A M X H X
        """.trimIndent(),
        """
        4
        EASY
        0 1 1 3 3 0 2 1 1
        0 E X D X D X X F
        1 X X X X F M X X
        2 X M X X X G C M
        2 X D X X X X G X
        1 X A X X X X X X
        3 M X C M X X X X
        2 X X X X G X X X
        1 X X X X X X X A
        
        """.trimIndent()
    )
//        """
//        3
//        EASY
//        0 2 0 2 1 5 1 0 1
//        1 M X F F D X X X
//        1 X X M X X X X X
//        4 M X M C M M X X
//        1 X X A C X X X M
//        2 X X X M M X H X
//        1 X X X X M A H A
//        1 X B X X M X H X
//        1 X X X X M X X X
//        """.trimIndent(),
//        """
//            4
//            EASY
//            0 0 2 3 1 1 3 2 0
//            1 C D M X X X X X
//            1 D D X X X M X X
//            0 D X A D X X E X
//            2 C X M X M X F X
//            0 X B B X X X X X
//            1 X M G X H E X X
//            5 X M M M X M M X
//            2 X X X X X M M X
//            """.trimIndent(),
//        """
//            5
//            EASY
//            0 1 0 1 2 1 1 1 5
//            3 X X X E X M M M
//            4 M X X M M G H M
//            1 X X X B X X X M
//            0 X B X X X D X X
//            0 D X X X F A X A
//            1 X X X X X F A M
//            2 X X M X X X X M
//            1 X X X M X X X X
//        """.trimIndent(),
//        """
//            6
//            EASY
//            0 3 1 3 0 1 1 1 2
//            1 X X M X X X X X
//            4 M B M X M X X M
//            0 E X X X X X X X
//            1 X D X X X X M A
//            1 A M F X X B X X
//            1 M X X D X X A G
//            3 M X M H X X F M
//            1 X X X X X M H X
//             """.trimIndent(),
//        """
//           7
//           EASY
//           0 5 1 2 0 0 0 1 3
//           4 M M M X X X X M
//           1 M D H D X X X X
//           1 A X F X X X X M
//           1 X X M X X X X X
//           2 M B X X X G M A
//           1 M X A X X X X X
//           0 X X X X X X X X
//           2 M X C X G X X M
//       """.trimIndent(),
//        """
//        8
//        EASY
//        0 2 3 3 1 0 0 1 2
//        3 E M M F X X X M
//        1 X M X F X X X X
//        1 X M H D X X X X
//        1 D X X M X X X X
//        2 M X B X G X X M
//        2 X X M X X X M A
//        0 X X X X X X X X
//        2 M A M X X X X X
//    """.trimIndent(),
//        """
//            9
//            EASY
//            0 1 1 3 1 0 3 2 1
//            2 X C M X X M X X
//            2 X X M C X X M X
//            2 X C C M X F E M
//            0 E X X X X X X X
//            2 X X M X X M X X
//            1 M X B X X E X H
//            2 A M X X X X M X
//            1 B X X X X M X H
//        """.trimIndent(),
//        """
//            10
//            EASY
//            0 1 3 2 3 1 1 1 0
//            2 X M G M X X X F
//            1 X M F X X X X X
//            1 X X M E F X X X
//            3 M B D M M X X F
//            3 X X M M G X M G
//            0 X X X X X X H X
//            0 X X F X X X X X
//            2 X M X X C M X X
//        """.trimIndent(),
//        """
//            11
//            EASY
//            0 1 3 1 2 0 0 3 2
//            3 M X M M X D G X
//            0 X E D E X F X X
//            3 C M X X X X M M
//            1 X X X X X X M X
//            0 X A X F X X X X
//            2 X M X X X H M X
//            1 X M B X G X X X
//            2 B X C M X X H M
//        """.trimIndent(),
//        """
//            12
//            EASY
//            0 3 2 1 2 2 1 0 1
//            2 M M X X F F X X
//            1 X X X X X M X X
//            3 C M X M M X H X
//            1 D X X X X X X M
//            1 M X X A X X X X
//            1 X D X X M X X X
//            2 X X M M B A X X
//            1 M X B A A X X X
//        """.trimIndent(),
//        """
//            13
//            EASY
//            0 0 3 1 1 2 1 1 3
//            1 D X X X X X X M
//            1 X X X E X X X M
//            2 X M X G M X X X
//            3 X M M X M F X G
//            3 X M B X C M M G
//            1 X X X M X X H X
//            0 X X X X X X X H
//            1 X X C X X X X M
//        """.trimIndent(),
//        """
//            14
//            EASY
//            0 1 0 4 1 2 2 1 1
//            2 X C M F X M X G
//            1 C X M H F X X X
//            3 M X G M M A X X
//            1 X X X X X M X X
//            1 X X M X X F X X
//            3 A B E X M C M M
//            0 A X X X X X X X
//            1 C X M A X X X X
//        """.trimIndent(),
//        """
//            15
//            EASY
//            0 1 1 1 1 5 1 0 2
//            1 E X E D M X X X
//            0 E X X B X X X X
//            2 X M X M D X F G
//            3 X X X X M M X M
//            3 X X M C M F X M
//            1 C B X X M X H X
//            1 M X X B X H X X
//            1 X H X X M X X X
//        """.trimIndent(),
//        """
//            16
//            EASY
//            0 3 1 1 1 4 1 1 0
//            0 D X D X X X X F
//            1 X X E X M X X F
//            2 M M X X A F X X
//            1 E X D M X X X X
//            2 X X E H M X M X
//            4 M X M A M M G X
//            1 M X X B X X X X
//            1 B X X X M X X X
//        """.trimIndent(),
//        """
//            17
//            EASY
//            0 2 1 3 1 1 1 0 3
//            2 M X D X X X X M
//            1 D M X X X X X X
//            2 B C M M F X G X
//            1 X X E X X M X E
//            2 X X M X X H G M
//            1 A C X B M X G X
//            0 X X X X X X X X
//            3 M X M X X X X M
//        """.trimIndent(),
//        """
//           18
//           EASY
//           0 1 2 1 2 0 2 1 3
//           0 X E X X X X X X
//           2 X M D M X X G X
//           1 C C H E X X X M
//           3 M B X M X M X X
//           3 C M B X F B M M
//           0 X X X X X X X X
//           2 C A M C X X X M
//           1 X X X X X M X X
//        """.trimIndent(),
//        """
//            19
//            EASY
//            0 3 0 1 2 1 2 0 3
//            1 X C X E X X X M
//            2 X X D M C M X X
//            2 M D X M X X X X
//            0 X X X X B X F X
//            0 X B X X D X H X
//            1 X X X X M X X X
//            4 M X M G C M X M
//            2 M X X X X X H M
//        """.trimIndent(),
//        """
//            20
//            EASY
//            0 1 2 0 1 2 0 2 4
//            2 X M G E E D M X
//            1 M X X D X X X X
//            1 X X X H X X X M
//            0 X X H D X X X X
//            2 C X B M M X X G
//            2 X E X X M X E M
//            1 X X X B X X X M
//            3 A M X A X X M M
//        """.trimIndent(),
//        """
//            21
//            EASY
//            0 0 3 1 2 2 1 0 3
//            2 X M X G X X F M
//            0 X X D X E X X X
//            2 X C M M X B X F
//            3 X M B M M X X G
//            3 X X X X M M X M
//            1 C E X X X X H M
//            0 X X B A X X X X
//            1 X M X X A X X X
//        """.trimIndent(),
//        """
//            22
//            EASY
//            0 2 1 3 0 1 1 1 3
//            1 D E X X X F X M
//            2 X M X G X M X X
//            0 E X X X X X X X
//            1 X X M B X X X X
//            2 M X X X X X M X
//            1 X H X X H B X M
//            2 X X M X X X X M
//            3 M G M X M X X G
//        """.trimIndent(),
//        """
//            23
//            EASY
//            0 0 1 1 3 4 1 1 1
//            1 X X X X M E D X
//            1 X X X X D X X M
//            2 X X D X M M X X
//            1 X B D X M X X X
//            4 X M M M M H X X
//            1 X X X M X X X H
//            1 X X X M X D X X
//            1 C X X X X X M X
//        """.trimIndent(),
//        """
//            24
//            EASY
//            0 1 1 1 3 1 1 4 0
//            1 X X X M X X X X
//            4 M X X M X M M X
//            0 X X X X X X X X
//            1 B X H E X X M X
//            1 D X F X E X M F
//            1 X M X X X X X X
//            3 X X M M X A M X
//            1 X X B X M G X X
//        """.trimIndent(),
//        """
//            25
//            EASY
//            0 3 0 1 2 2 0 1 3
//            3 M X G M X X M F
//            2 X C M X X X X M
//            0 X D X H X X F X
//            0 E X X X X X X X
//            2 X X X C M X X M
//            1 X B X X M X X G
//            3 M X X M X X X M
//            1 M X X A X X X X
//        """.trimIndent(),
//        """
//            26
//            EASY
//            0 0 3 2 1 0 2 2 2
//            1 X X M X X E X X
//            2 C C M X F M X X
//            2 X D A M X D F M
//            2 X M X X X G M X
//            2 X M X X X G M X
//            1 X B H X X B H M
//            1 X X X B H M X X
//            1 X M X X X X X X
//        """.trimIndent(),
//        """
//            27
//            EASY
//            0 3 2 1 2 2 0 1 1
//            0 E X F X X X X X
//            2 X X M G M X X X
//            2 M B X E X X X M
//            1 E M X X X X H X
//            3 M D X X M X M X
//            3 M M X M G X X X
//            1 X X X M B X X X
//            0 X X X X X H X H
//        """.trimIndent(),
//        """
//            28
//            EASY
//            0 0 1 1 3 2 0 2 3
//            1 D X F M F X G X
//            2 C M X E M X X X
//            2 X X M D X X M E
//            1 X X X X X X X M
//            2 X B X M X X X M
//            2 X X X B X B M M
//            2 X X X M M X X A
//            0 X X B X X X X X
//        """.trimIndent(),
//        """
//            29
//            EASY
//            0 1 1 1 3 1 4 1 0
//            3 X M D X X M M X
//            1 B X X M X X X X
//            3 M X B M X M X X
//            2 D X M X X M G X
//            0 X X X X X A X X
//            1 X B X M G X X X
//            0 X X X X X H X X
//            2 X X A X M M X X
//        """.trimIndent(),
//        """
//            30
//            EASY
//            0 1 1 2 2 2 2 1 1
//            1 E C X X X M X X
//            1 X X X M X A X X
//            1 D C F X M X X X
//            1 X X E B D X X M
//            2 M X X D X M X X
//            4 X M M M M X X X
//            0 X X X X X D X X
//            2 X X M B X C M X
//        """.trimIndent(),
//        """
//            31
//            MEDIUM
//            0 1 3 2 2 1 0 3 0
//            1 X X X M D X X X
//            1 X X F C X X M F
//            3 X M M X X X M X
//            2 M D M A X X X X
//            0 X X X F X X X H
//            1 X M B X X X X X
//            3 X M X X M X M X
//            1 X B H M A B X G
//        """.trimIndent(),
//        """
//            32
//            MEDIUM
//            0 3 0 4 1 0 1 1 2
//            2 M X M X X X X F
//            1 M X X X X X X X
//            1 C H X X H X X M
//            2 X X F M D X X M
//            1 X X M X X B X X
//            3 M X M D X X M G
//            1 X X M X X X X X
//            1 B C X X X M X H
//        """.trimIndent(),
//        """
//            33
//            MEDIUM
//            0 3 2 3 0 0 3 0 1
//            2 M M F X X X F X
//            1 A X E X X X X M
//            2 M C X G X M X X
//            1 X A M X X X X X
//            0 X X F X X X X X
//            3 X M M X X M X X
//            1 M X B X X H X X
//            2 A X M G X M X X
//        """.trimIndent(),
//        """
//            34
//            MEDIUM
//            0 1 0 1 3 2 2 0 3
//            1 X X X X M X F X
//            0 X X X X X X X X
//            1 X X X X F X X M
//            4 M X M M G X X M
//            3 X X C X M M F M
//            1 X X X M X E X X
//            1 X X X H H M X H
//            1 X B X M X X H X
//        """.trimIndent(),
//        """
//            35
//            MEDIUM
//            0 1 1 0 3 5 1 0 1
//            1 X D X X M X X X
//            1 X X X X M X X G
//            1 X X B D M H X F
//            1 X D X M X X X X
//            2 X B X E M M X X
//            1 X X D M X H X X
//            1 X X X X X C X M
//            4 M M X M M X X X
//        """.trimIndent(),
//        """
//            36
//            MEDIUM
//            0 2 1 1 2 3 0 2 1
//            0 X X X X X X X F
//            1 X X X X X X M X
//            1 X X M X F D X X
//            0 X B D H X F A X
//            3 M X X G M X X M
//            2 X M X B M X X X
//            2 X X X M M X X X
//            3 M B X M C X M X
//        """.trimIndent(),
//        """
//            37
//            MEDIUM
//            0 2 2 1 0 2 3 0 2
//            1 D X X X X M X X
//            1 X M X D X X X X
//            2 M A X B X X X M
//            1 E X X X M X X E
//            0 A X X B E X X A
//            3 M C X D F M G M
//            1 X M X X X X X X
//            3 X C M G M M X X
//        """.trimIndent(),
//        """
//            38
//            MEDIUM
//            0 2 2 1 0 0 2 3 2
//            0 X X F X X X X X
//            3 M M C X X C X M
//            1 X A X X X M X X
//            1 D X X X X B M X
//            2 C M X X X D M X
//            2 C X M X X M E X
//            2 M X G X X X X M
//            1 X X X B X X M X
//        """.trimIndent(),
//        """
//                39
//                MEDIUM
//                0 2 2 2 1 2 2 0 1
//                1 X X X C X M X X
//                2 X M M E X X X X
//                1 X X X X M A X X
//                2 M X B X M X X X
//                0 X X X X X X X X
//                2 B M A M X E G H
//                1 X B X X X M H X
//                3 M X M X X H X M
//            """.trimIndent(),
//        """
//                    40
//                    MEDIUM
//                    0 1 0 6 1 0 2 1 1
//                    2 X D X X X M M X
//                    2 M X D M F X H X
//                    1 X X M X X G X F
//                    1 D B M X D X X X
//                    3 D X M X X M H M
//                    1 B X M X X X X G
//                    1 X D M X X H X X
//                    1 X X M X X X X X
//            """.trimIndent(),
//        """
//                    41
//                    MEDIUM
//                    0 3 1 1 1 1 2 1 2
//                    2 D C M X X M X X
//                    3 X M F M B X X M
//                    1 M X X X X X X G
//                    1 M X X X X H X F
//                    3 M X H X M G X M
//                    1 X X X X D M X X
//                    0 X X H H X X X X
//                    1 A X X X X A M X
//            """.trimIndent(),
//        """
//                    42
//                    MEDIUM
//                    0 1 1 1 2 2 2 2 1
//                    5 M X C M M M X M
//                    2 C X M C E M X X
//                    0 D B X X X X X X
//                    1 C X X A D X M X
//                    1 B M X X X B A X
//                    1 X X X X X X M X
//                    1 X X X B M X X X
//                    1 C X X M H X X G
//            """.trimIndent(),
//        """
//                    43
//                    MEDIUM
//                    0 0 0 1 1 5 1 2 2
//                    2 X X X X M G M X
//                    1 X X X D M F X X
//                    1 X X X X M X D X
//                    2 X B X C M X X M
//                    1 X B X X E X H M
//                    2 X X M M X X H X
//                    0 X B B X X B X X
//                    3 X X C X M M M X
//            """.trimIndent(),
//        """
//                    44
//                    MEDIUM
//                    0 1 1 1 1 2 3 2 1
//                    1 X X X X M F X X
//                    1 D X X X X E M X
//                    4 M X X M D M M X
//                    2 D B X X X M G M
//                    0 X X X X X X H X
//                    3 X E M X M M G X
//                    0 X X A X H X X X
//                    1 X M B B X X X X
//            """.trimIndent(),
//        """
//                    45
//                    MEDIUM
//                    0 2 2 0 0 3 2 1 2
//                    3 D C C X M M M X
//                    3 M M D X X X X M
//                    1 X M X X X X H X
//                    0 D X X X X X X H
//                    1 M B D X X F X X
//                    1 X B X X M X X X
//                    1 A X X X X H X M
//                    2 X X X X M M X G
//            """.trimIndent(),
//        """
//                    46
//                    HARD
//                    0 1 1 1 4 2 0 2 1
//                    3 D D M M E X M G
//                    0 X D D X D D F X
//                    2 X X X M G X M X
//                    2 X M X E M X X X
//                    4 M X H M M X X M
//                    0 X X X H X X X X
//                    0 X B X X X X H X
//                    1 X X X M X X H X
//            """.trimIndent(),
//        """
//                    47
//                    HARD
//                    0 2 2 1 0 1 1 1 4
//                    3 M E X F M G X M
//                    0 X X X X X X X X
//                    1 E M D X F F X X
//                    1 X B X X X M X E
//                    1 C X X X D X X M
//                    2 X X M B H X X M
//                    1 M X X X X X X X
//                    3 B M X X G B M M
//            """.trimIndent(),
//        """
//                    48
//                    HARD
//                    0 1 1 3 3 0 2 1 1
//                    0 E X D X D X X F
//                    1 X X X X F M X X
//                    2 X M X X X G C M
//                    2 X D M X X M G X
//                    1 X A X M X X X X
//                    3 M X C M X X M X
//                    2 X X M M G X X X
//                    1 X X M X X X X A
//            """.trimIndent(),
//        """
//                    49
//                    HARD
//                    0 4 2 3 1 1 0 1 0
//                    1 X X M X X X E X
//                    3 M C M G F X M X
//                    2 M M X X X G F X
//                    1 X X X M X X X X
//                    1 X M X X X H H X
//                    2 M B H X M X H X
//                    1 C X M B X X H X
//                    1 M A X X X X A X
//            """.trimIndent(),
//        """
//                    50
//                    HARD
//                    0 3 2 1 1 0 2 1 2
//                    1 X X M X X X G E
//                    2 E M X X X X X M
//                    2 X X X X X M B M
//                    1 M X X X X H X X
//                    1 X X X X H M X X
//                    0 X X X X X X X X
//                    3 M M X X X A M A
//                    2 M C X M H B X A
//            """.trimIndent()
//
//    )

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
        val length = 4
        val boards = arrayOfNulls<Board>(length)

        for (i in 0 until length)
            boards[i] = boardFromString(stringz[i])

        return boards
    }
}