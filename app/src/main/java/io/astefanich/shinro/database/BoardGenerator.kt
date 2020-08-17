package io.astefanich.shinro.database

import io.astefanich.shinro.domain.*
import timber.log.Timber
import java.lang.IllegalStateException

class BoardGenerator(private val boardCount: BoardCount) {

    init {
        Timber.i("BOARD GENERATOR CREATED")
    }

    private fun boardFromString(str: String): Board {
        var numMarbles = 0
        val lines = str.lines()
        val boardNum = lines[0].toInt()
        val difficulty = Difficulty.valueOf(lines[1])
        Timber.i("difficulty for board $boardNum is $difficulty: ${difficulty.repr}")
        val cells = Array(9) { Array(9) { Cell(" ") } }
        for (i in 0..8) {
            val chars = lines[i + 2].split(" ")
            for (j in 0..8) {
                val actual = chars[j]
                if (actual == "M")
                    numMarbles += 1
                if (actual == "M" || actual == "X")
                    cells[i][j] = Cell(" ", actual)
                else
                    cells[i][j] = Cell(actual)
            }
        }
        if (numMarbles != 12)
            throw IllegalStateException("Board $boardNum has $numMarbles !!!")
        return Board(boardNum = boardNum, difficulty = difficulty, cells = cells)

    }

    fun genBoards(): Array<Board?> {
        val length = boardCount.value
        val boards = arrayOfNulls<Board>(length)

        for (i in 0 until length)
            boards[i] = boardFromString(stringz[i])

        return boards
    }


    val stringz: Array<String> by lazy {
        arrayOf(
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
        """.trimIndent(),
        """
        11
        EASY
        0 1 3 1 2 0 0 3 2
        3 M X M M X D G X
        0 X E D E X F X X
        3 C M X X X X M M
        1 X X X X X X M X
        0 X A X F X X X X
        2 X M X X X H M X
        1 X M B X G X X X
        2 B X C M X X H M
        """.trimIndent(),
        """
        12
        EASY
        0 3 2 1 2 2 1 0 1
        2 M M X X F F X X
        1 X X X X X M X X
        3 C M X M M X H X
        1 D X X X X X X M
        1 M X X A X X X X
        1 X D X X M X X X
        2 X X M M B A X X
        1 M X B A A X X X
        """.trimIndent(),
        """
        13
        EASY
        0 0 3 1 1 2 1 1 3
        1 D X X X X X X M
        1 X X X E X X X M
        2 X M X G M X X X
        3 X M M X M F X G
        3 X M B X C M M G
        1 X X X M X X H X
        0 X X X X X X X H
        1 X X C X X X X M
        """.trimIndent(),
        """
        14
        EASY
        0 1 0 4 1 2 2 1 1
        2 X C M F X M X G
        1 C X M H F X X X
        3 M X G M M A X X
        1 X X X X X M X X
        1 X X M X X F X X
        3 A B E X M C M M
        0 A X X X X X X X
        1 C X M A X X X X
        """.trimIndent(),
        """
        15
        EASY
        0 1 1 1 1 5 1 0 2
        1 E X E D M X X X
        0 E X X B X X X X
        2 X M X M D X F G
        3 X X X X M M X M
        3 X X M C M F X M
        1 C B X X M X H X
        1 M X X B X H X X
        1 X H X X M X X X
        """.trimIndent(),
        """
        16
        EASY
        0 3 1 1 1 4 1 1 0
        0 D X D X X X X F
        1 X X E X M X X F
        2 M M X X A F X X
        1 E X D M X X X X
        2 X X E H M X M X
        4 M X M A M M G X
        1 M X X B X X X X
        1 B X X X M X X X
        """.trimIndent(),
        """
        17
        EASY
        0 2 1 3 1 1 1 0 3
        2 M X D X X X X M
        1 D M X X X X X X
        2 B C M M F X G X
        1 X X E X X M X E
        2 X X M X X H G M
        1 A C X B M X G X
        0 X X X X X X X X
        3 M X M X X X X M
        """.trimIndent(),
        """
        18
        EASY
        0 1 2 1 2 0 2 1 3
        0 X E X X X X X X
        2 X M D M X X G X
        1 C C H E X X X M
        3 M B X M X M X X
        3 C M B X F B M M
        0 X X X X X X X X
        2 C A M C X X X M
        1 X X X X X M X X
        """.trimIndent(),
        """
        19
        EASY
        0 3 0 1 2 1 2 0 3
        1 X C X E X X X M
        2 X X D M C M X X
        2 M D X M X X X X
        0 X X X X B X F X
        0 X B X X D X H X
        1 X X X X M X X X
        4 M X M G C M X M
        2 M X X X X X H M
        """.trimIndent(),
        """
        20
        EASY
        0 1 2 0 1 2 0 2 4
        2 X M G E E D M X
        1 M X X D X X X X
        1 X X X H X X X M
        0 X X H D X X X X
        2 C X B M M X X G
        2 X E X X M X E M
        1 X X X B X X X M
        3 A M X A X X M M
        """.trimIndent(),
        """
        21
        EASY
        0 0 3 1 2 2 1 0 3
        2 X M X G X X F M
        0 X X D X E X X X
        2 X C M M X B X F
        3 X M B M M X X G
        3 X X X X M M X M
        1 C E X X X X H M
        0 X X B A X X X X
        1 X M X X A X X X
        """.trimIndent(),
        """
        22
        EASY
        0 2 1 3 0 1 1 1 3
        1 D E X X X F X M
        2 X M X G X M X X
        0 E X X X X X X X
        1 X X M B X X X X
        2 M X X X X X M X
        1 X H X X H B X M
        2 X X M X X X X M
        3 M G M X M X X G
        """.trimIndent(),
        """
        23
        EASY
        0 0 1 1 3 4 1 1 1
        1 X X X X M E D X
        1 X X X X D X X M
        2 X X D X M M X X
        1 X B D X M X X X
        4 X M M M M H X X
        1 X X X M X X X H
        1 X X X M X D X X
        1 C X X X X X M X
        """.trimIndent(),
        """
        24
        EASY
        0 1 1 1 3 1 1 4 0
        1 X X X M X X X X
        4 M X X M X M M X
        0 X X X X X X X X
        1 B X H E X X M X
        1 D X F X E X M F
        1 X M X X X X X X
        3 X X M M X A M X
        1 X X B X M G X X
        """.trimIndent(),
        """
        25
        EASY
        0 3 0 1 2 2 0 1 3
        3 M X G M X X M F
        2 X C M X X X X M
        0 X D X H X X F X
        0 E X X X X X X X
        2 X X X C M X X M
        1 X B X X M X X G
        3 M X X M X X X M
        1 M X X A X X X X
        """.trimIndent(),
        """
        26
        EASY
        0 0 3 2 1 0 2 2 2
        1 X X M X X E X X
        2 C C M X F M X X
        2 X D A M X D F M
        2 X M X X X G M X
        2 X M X X X G M X
        1 X B H X X B H M
        1 X X X B H M X X
        1 X M X X X X X X
        """.trimIndent(),
        """
        27
        EASY
        0 3 2 1 2 2 0 1 1
        0 E X F X X X X X
        2 X X M G M X X X
        2 M B X E X X X M
        1 E M X X X X H X
        3 M D X X M X M X
        3 M M X M G X X X
        1 X X X M B X X X
        0 X X X X X H X H
        """.trimIndent(),
        """
        28
        EASY
        0 0 1 1 3 2 0 2 3
        1 D X F M F X G X
        2 C M X E M X X X
        2 X X M D X X M E
        1 X X X X X X X M
        2 X B X M X X X M
        2 X X X B X B M M
        2 X X X M M X X A
        0 X X B X X X X X
        """.trimIndent(),
        """
        29
        EASY
        0 1 1 1 3 1 4 1 0
        3 X M D X X M M X
        1 B X X M X X X X
        3 M X B M X M X X
        2 D X M X X M G X
        0 X X X X X A X X
        1 X B X M G X X X
        0 X X X X X H X X
        2 X X A X M M X X
        """.trimIndent(),
        """
        30
        EASY
        0 1 1 2 2 2 2 1 1
        1 E C X X X M X X
        1 X X X M X A X X
        1 D C F X M X X X
        1 X X E B D X X M
        2 M X X D X M X X
        4 X M M M M X X X
        0 X X X X X D X X
        2 X X M B X C M X
        """.trimIndent(),
        """
        31
        EASY
        0 1 2 1 1 3 2 1 1
        1 X X X X M D X G
        3 X M X D X M M E
        1 X D X X M A X X
        2 X X M D M X X X
        3 M M X B H M X X
        0 X X B X X X X X
        0 B X X X X X X X
        2 X X X M A X X M
        """.trimIndent(),
        """
        32
        EASY
        0 2 0 1 2 2 2 2 1
        2 X X X M M F G G
        1 X X X X X M F X
        0 X D X X X X X X
        2 M X X G M A X X
        2 X X M E X H M X
        2 M X X H C M X X
        1 X B X X X X H M
        2 A X X M X X M G
        """.trimIndent(),
        """
        33
        EASY
        0 0 3 1 2 1 2 1 2
        2 X C M E G M X X
        3 X M D M X X X M
        1 C C X X X M X H
        1 D E X M X X X X
        1 X M X X X X X H
        1 X M X X X X X F
        2 X X X D X X M M
        1 X X X X M X X X
        """.trimIndent(),
        """
        34
        EASY
        0 2 1 1 2 1 0 2 3
        1 X X X M X X F F
        1 X X X D X X X M
        2 C X M X X X X M
        1 M X X X X X H F
        3 B X X M M X M A
        3 M M G X X X X M
        0 X X X X X X H X
        1 C X C X X X M H
        """.trimIndent(),
        """
        35
        EASY
        0 1 1 1 1 3 2 2 1
        0 X X X E E X X X
        1 X X X D E X X M
        2 X X X X M C M X
        2 X C X X M M A H
        4 D X M M M M X G
        2 X M X X X X M X
        1 M X B X G X X X
        0 X A X X B X X X
        """.trimIndent(),
        """
        36
        EASY
        0 3 1 2 1 2 2 0 1
        2 M X X X X M X X
        1 D X M X X X X F
        2 M M X X X A X X
        3 A X B M M X X M
        1 X X X X M X X X
        1 M X E A X E X X
        0 B X X A X X X H
        2 X X M A X M X H
        """.trimIndent(),
        """
        37
        EASY
        0 2 1 0 0 1 3 1 4
        1 C X D F X M X G
        2 X M X X X F X M
        0 X X X X X X X X
        3 M C D C M M X X
        1 C X X X X D X M
        2 A X X X C M H M
        2 M X B X G X H M
        1 X X X X X X M X
        """.trimIndent(),
        """
        38
        EASY
        0 2 1 1 1 0 2 2 3
        1 E X X C D M X E
        2 X X X X X M E M
        0 E X X X X A X X
        1 M X D B X X F X
        0 X X X X B X X X
        3 B M X M X X M A
        2 M X X X X X X M
        3 A B M X X X M M
        """.trimIndent(),
        """
        39
        EASY
        0 1 3 1 3 1 2 1 0
        1 C X X M X X X X
        1 X M X X X F X X
        1 D D X A M H X X
        1 B X E X X M X G
        2 C X M X X M X G
        1 X M D X X F X X
        3 M M X M X D X X
        2 X X B M X X M X
        """.trimIndent(),
        """
        40
        EASY
        0 1 2 1 1 1 1 3 2
        1 M X X F X X E F
        4 C M M X X M E M
        1 B X D X X X M X
        0 D E X X X X X X
        1 X X X X X X E M
        1 X A F X M X H X
        3 X M X M X H M X
        1 X A H X X X M X
        """.trimIndent(),
        """
        41
        EASY
        0 1 1 1 0 1 2 4 2
        1 X X X X X C M X
        1 X X X X X X M E
        1 X X M X X X X X
        2 X B X X B F M M
        2 M X X X G M X X
        1 X X C X M X A G
        3 X X X X C M M M
        1 X M X X X X X X
        """.trimIndent(),
        """
        42
        EASY
        0 2 1 1 1 2 1 3 1
        0 X X X D X E X X 
        1 X X M D X X X X
        1 X X X E M D D X
        4 M M X G H F M M
        4 M X G X M M M X
        0 X X H X B F X X
        1 X X X X X X M X
        1 X X X M X A X X
        """.trimIndent(),
        """
        43
        EASY
        0 1 5 1 1 1 1 1 1
        1 X E M X X X F X
        1 X M F X X X X X
        0 B X X X X X X X
        4 M M X M X X M X
        2 X M X D M X X X
        1 X M X X X X A H
        2 X M G B X M D X
        1 X X X X X H A M
        """.trimIndent(),
        """
        44
        EASY
        0 1 0 2 1 1 1 5 1
        1 X X X X X X M X
        1 X X M X X G X F
        2 X X A X M X M X
        2 X X M X D X M X
        0 D B X X X X X H
        2 M C X X C X M X
        2 C X X X D X M M
        2 X X X M G M X X
        """.trimIndent(),
        """
        45
        EASY
        0 1 2 1 2 1 2 2 1
        1 X X X M E D F X
        4 X M X X X M M M
        2 M B M X X G B X
        0 X X X X X X X F
        0 B X X A X X X H
        1 X E X X X X M X
        3 C C X M M M G X
        1 X M X A X X X X
        """.trimIndent(),
        """
        1
        MEDIUM
        0 1 3 2 2 1 0 3 0
        1 X X X M D X X X
        1 X X F C X X M F
        3 X M M X X X M X
        2 M D M A X X X X
        0 X X X F X X X H
        1 X M B X X X X X
        3 X M X X M X M X
        1 X B H M A B X G
        """.trimIndent(),
        """
        2
        MEDIUM
        0 3 0 4 1 0 1 1 2
        2 M X M X X X X F
        1 M X X X X X X X
        1 C H X X H X X M
        2 X X F M D X X M
        1 X X M X X B X X
        3 M X M D X X M G
        1 X X M X X X X X
        1 B C X X X M X H
        """.trimIndent(),
        """
        3
        MEDIUM
        0 3 2 3 0 0 3 0 1
        2 M M F X X X F X
        1 A X E X X X X M
        2 M C X G X M X X
        1 X A M X X X X X
        0 X X F X X X X X
        3 X M M X X M X X
        1 M X B X X H X X
        2 A X M G X M X X
        """.trimIndent(),
        """
        4
        MEDIUM
        0 1 0 1 3 2 2 0 3
        1 X X X X M X F X
        0 X X X X X X X X
        1 X X X X F X X M
        4 M X M M G X X M
        3 X X C X M M F M
        1 X X X M X E X X
        1 X X X H H M X H
        1 X B X M X X H X
        """.trimIndent(),
        """
        5
        MEDIUM
        0 1 1 0 3 5 1 0 1
        1 X D X X M X X X
        1 X X X X M X X G
        1 X X B D M H X F
        1 X D X M X X X X
        2 X B X E M M X X
        1 X X D M X H X X
        1 X X X X X C X M
        4 M M X M M X X X
        """.trimIndent(),
        """
        6
        MEDIUM
        0 2 1 1 2 3 0 2 1
        0 X X X X X X X F
        1 X X X X X X M X
        1 X X M X F D X X
        0 X B D H X F A X
        3 M X X G M X X M
        2 X M X B M X X X
        2 X X X M M X X X
        3 M B X M C X M X
        """.trimIndent(),
        """
        7
        MEDIUM
        0 2 2 1 0 2 3 0 2
        1 D X X X X M X X
        1 X M X D X X X X
        2 M A X B X X X M
        1 E X X X M X X E
        0 A X X B E X X A
        3 M C X D F M G M
        1 X M X X X X X X
        3 X C M G M M X X
        """.trimIndent(),
        """
        8
        MEDIUM
        0 2 2 1 0 0 2 3 2
        0 X X F X X X X X
        3 M M C X X C X M
        1 X A X X X M X X
        1 D X X X X B M X
        2 C M X X X D M X
        2 C X M X X M E X
        2 M X G X X X X M
        1 X X X B X X M X
        """.trimIndent(),
        """
        9
        MEDIUM
        0 2 2 2 1 2 2 0 1
        1 X X X C X M X X
        2 X M M E X X X X
        1 X X X X M A X X
        2 M X B X M X X X
        0 X X X X X X X X
        2 B M A M X E G H
        1 X B X X X M H X
        3 M X M X X H X M
        """.trimIndent(),
        """
        10
        MEDIUM
        0 1 0 6 1 0 2 1 1
        2 X D X X X M M X
        2 M X D M F X H X
        1 X X M X X G X F
        1 D B M X D X X X
        3 D X M X X M H M
        1 B X M X X X X G
        1 X D M X X H X X
        1 X X M X X X X X
        """.trimIndent(),
        """
        11
        MEDIUM
        0 3 1 1 1 1 2 1 2
        2 D C M X X M X X
        3 X M F M B X X M
        1 M X X X X X X G
        1 M X X X X H X F
        3 M X H X M G X M
        1 X X X X D M X X
        0 X X H H X X X X
        1 A X X X X A M X
        """.trimIndent(),
        """
        12
        MEDIUM
        0 1 1 1 2 2 2 2 1
        5 M X C M M M X M
        2 C X M C E M X X
        0 D B X X X X X X
        1 C X X A D X M X
        1 B M X X X B A X
        1 X X X X X X M X
        1 X X X B M X X X
        1 C X X M H X X G
        """.trimIndent(),
        """
        13
        MEDIUM
        0 0 0 1 1 5 1 2 2
        2 X X X X M G M X
        1 X X X D M F X X
        1 X X X X M X D X
        2 X B X C M X X M
        1 X B X X E X H M
        2 X X M M X X H X
        0 X B B X X B X X
        3 X X C X M M M X
        """.trimIndent(),
        """
        14
        MEDIUM
        0 1 1 1 1 2 3 2 1
        1 X X X X M F X X
        1 D X X X X E M X
        4 M X X M D M M X
        2 D B X X X M G M
        0 X X X X X X H X
        3 X E M X M M G X
        0 X X A X H X X X
        1 X M B B X X X X
        """.trimIndent(),
        """
        15
        MEDIUM
        0 2 2 0 0 3 2 1 2
        3 D C C X M M M X
        3 M M D X X X X M
        1 X M X X X X H X
        0 D X X X X X X H
        1 M B D X X F X X
        1 X B X X M X X X
        1 A X X X X H X M
        2 X X X X M M X G
        """.trimIndent(),
        """
        16
        MEDIUM
        0 0 3 2 1 3 1 1 1
        2 X M X X M X X X
        1 X X M X X H X X
        3 X M M X M X X G
        1 X X X X X X M X
        2 X M C C X H X M
        1 X D A D B M X X
        1 X X X X M B X X
        1 X X X M X X X X
        """.trimIndent(),
        """
        17
        MEDIUM
        0 2 1 2 2 0 2 1 2
        0 X X X X F X X X
        3 C M D M X M G X
        3 M X X F D M X M
        1 X X E M X F X X
        1 X X M X X A X X
        3 M X X H B X M M
        1 X X M X X A X X
        0 X X X X X X X X
        """.trimIndent(),
        """
        18
        MEDIUM
        0 2 0 2 1 2 2 3 0
        2 M X X M D X X X
        1 X X X D H X M X
        2 D X X X M X M X
        0 B X X X B X X X
        1 X X X X X M X H
        0 X X X X X X X X
        4 M X M B M X M X
        2 C X M X X M G X
        """.trimIndent(),
        """
        19
        MEDIUM
        0 1 1 2 1 1 2 3 1
        3 M G M X X M E X
        1 C X X X X X M X
        0 X X A D X X X X
        1 C M X X X X F X
        1 X B X X X M X X
        0 X X X X X X X X
        1 X X X X H X M H
        5 X X M M M X M M
        """.trimIndent(),
        """
        20
        MEDIUM
        0 1 4 1 2 1 0 1 2
        1 X M X X G X E X
        1 C X X X X X X M
        4 C M M M M X X G
        0 X D D H X X X X
        0 X X X X X X X A
        1 C X X M X X G G
        3 M M X A X X G M
        2 X M X C X X M X
        """.trimIndent(),
        """
        21
        MEDIUM
        0 2 1 0 2 2 0 4 1
        2 M C X X X G M X
        4 M M X D F X M M
        2 X X X M M G X X
        0 X X D X A X X X
        0 X X X D X X X X
        2 X X C M M X X X
        1 X X X B H H M G
        1 X X X X X X M X
        """.trimIndent(),
        """
        22
        MEDIUM
        0 1 1 1 4 3 1 1 0
        1 X D M X X X X G
        1 X X F X M X X X
        2 X M X B F M X X
        3 M X X M M H X X
        2 X X X M M F X X
        1 C X X M H X X G
        2 X X X M X C M X
        0 X X X X X X H X
        """.trimIndent(),
        """
        23
        MEDIUM
        0 1 3 1 2 2 0 1 2
        2 C M C X X X X M
        1 X M X D X F X X
        3 X X M M M B G X
        1 X M D H X X X X
        0 X X X F X H X X
        2 X C X M M X X X
        2 X F B B X X M M
        1 M X X X X X X X
        """.trimIndent(),
        """
        24
        MEDIUM
        0 1 1 1 0 3 2 3 1
        2 M X E G X X M X
        0 D X X X F X X X
        2 C D X X M M F X
        2 X X X X M M X G
        2 X M C X X X M X
        3 B B M X M H H M
        1 X X X B X X M X
        0 X X X X A X X X
        """.trimIndent(),
        """
        25
        MEDIUM
        0 1 4 2 0 1 1 2 1
        3 C M M X X X M F
        2 X M M X D X X X
        0 B X X X X X X X
        1 X X X X D D M X
        1 X A C X X M X X
        2 E X X C M C X M
        2 M M X B X H X G
        1 X M A X X X X X
        """.trimIndent(),
        """
        26
        MEDIUM
        0 2 2 0 1 1 1 2 3
        3 X M G X X F M M
        2 X E X M X X X M
        1 M X D X X X X X
        1 X E F B H X X M
        2 X X X D M M X X
        1 M X X F X X X H
        0 X X X X X X A X
        2 X M B G X H M X
        """.trimIndent(),
        """
        27
        MEDIUM
        0 3 3 1 1 2 0 1 1
        1 X M D D X X X F
        3 M M C X M X X X
        1 M X X X E X X X
        0 D X X X X X X X
        3 X M X M X F M X
        0 X X X H F X X X
        3 M D X X M G X M
        1 X X M X X X X H
        """.trimIndent(),
        """
        28
        MEDIUM
        0 1 0 2 0 1 2 4 2
        1 X X X X D X M X
        1 X X M X X D E X
        1 X X X X X D M X
        1 X X A B X E X M
        4 M X X X M D M M
        1 X X E X X E M X
        2 C X M X B M X X
        1 X B X H B M X X
        """.trimIndent(),
        """
        29
        MEDIUM
        0 2 2 1 0 2 0 3 2
        1 X X X D F X M X
        1 M X X X X X X X
        1 X X M D X X F X
        1 C X X X X F M G
        3 C M B X M X M G
        0 X X X X H X X X
        2 X X X X M X X M
        3 M M X B A G X M
        """.trimIndent(),
        """
        30
        MEDIUM
        0 0 4 3 0 1 2 1 1
        3 X M M X X M G X
        0 X X E X X E X X
        0 X X X F D X X X
        1 X M X X X X X X
        1 X M X X X X X X
        2 B X M H X X X M
        1 X X A X X A M X
        4 X M M X M M G X
        """.trimIndent(),
        """
        31
        MEDIUM
        0 2 1 2 0 2 3 1 1
        0 X X X X E X F F
        1 X X M F X X X X
        1 C D C X X M X X
        1 X X X X X M X X
        2 M X X G M H X G
        6 M M M X M M M X
        1 X X X X X H C M
        0 B B X X X X H X
        """.trimIndent(),
        """
        32
        MEDIUM
        0 2 0 1 1 3 2 1 2
        4 M C D M M X F M
        1 X D X X X M X X
        0 E X X X X X X X
        1 C X X D F M X X
        0 A X X X X A X X
        1 C X X C M X X X
        1 X X X X X X M X
        4 M B M X M X H M
        """.trimIndent(),
        """
        33
        MEDIUM
        0 2 4 0 1 2 1 1 1
        1 E X X M E X X F
        2 E M X D X X M X
        1 M E X X X X F H
        1 X X X A C M X X
        1 X X X X M X X G
        3 C M X X M X X M
        2 M M X X H X H X
        1 X M X H A X X X
        """.trimIndent(),
        """
        34
        MEDIUM
        0 2 1 2 2 1 1 1 2
        3 X D C X M C M M
        3 M G M X X M X X 
        0 X X X E X X X E
        1 X X X M X F X X
        0 X X X F H X F X
        0 X X X A X X X X
        1 X X X X X X X M
        4 M M M M A X X H
        """.trimIndent(),
        """
        35
        MEDIUM
        0 1 0 2 1 3 3 1 1
        1 D X X X X M X G
        3 M X M M X D X E
        2 X X C X C M M G
        1 B X X X M A X X
        0 X B E X X X H X
        3 X X M X C M X M
        1 X X X X M H X X
        1 X X X X M X X X
        """.trimIndent(),
        """
        36
        MEDIUM
        0 1 2 1 2 3 1 1 1
        1 X X F X M D X X
        2 X M X X C M X X
        2 X M X D X C G M
        2 X C H M M G X X
        2 X X C M M G X X
        2 M X G G A C M X
        1 X X M X X X X X
        0 X X B X X X X X
        """.trimIndent(),
        """
        37
        MEDIUM
        0 1 1 3 1 1 3 1 1
        1 X X X X X X M X
        2 X M M F X X X X
        1 X X D X B M X X
        1 X X X H B M A G
        2 M X M D X G X X
        4 X X M H M M X M
        1 X X X M X X X X
        0 X B X X X B X X
        """.trimIndent(),
        """
        38
        MEDIUM
        0 1 1 0 3 2 3 1 1
        4 X M X X M M M X
        1 X X X M E X X X
        3 M C X C C M X M
        0 X X X B B X X X
        1 C X X X X M H X
        1 X X D X M F X X
        1 B X X M X A X X
        1 X X X M X X H X
        """.trimIndent(),
        """
        39
        MEDIUM
        0 2 0 2 1 0 5 0 2
        1 X D X X X M F X
        1 C X X X X M X X
        2 D X C C X M X M
        1 D X M X B X X X
        0 X X X B X X X X
        4 M X M M X M X X
        1 A X B X X X X M
        2 M X X X X M H X
        """.trimIndent(),
        """
        40
        MEDIUM
        0 2 1 1 2 1 1 3 1
        2 M E M F X X X X
        1 X X X X X X M X
        3 C M X C G B M M
        0 X X X X X X X H
        0 D X D X X X H F
        2 X X D M X C M X
        2 M X B C X M X G
        2 X X X M M X X X
        """.trimIndent(),
        """
        41
        MEDIUM
        0 3 1 2 2 0 1 1 2
        1 D X X M X D X X
        5 D M X M X M M M
        0 B X X A F B X X
        2 M B M X X X X G
        1 C X M X X D X X
        1 M X X B X X X A
        1 X X X X X X X M
        1 M X X X X X A H
        """.trimIndent(),
        """
        42
        MEDIUM
        0 2 0 2 1 0 5 1 1
        2 X C M X X M X X
        2 M X X X X M X X
        1 X X X X X M X F
        1 E D X X X A M X
        1 C X E X X E X M
        2 B X X M H M G H
        1 X X M X X X X X
        2 M X X H X M X X
        """.trimIndent(),
        """
        43
        MEDIUM
        0 0 2 2 1 2 0 3 2
        3 C M D X X X M M
        3 X X D M M X M G
        2 X M M X X D G X
        0 X A D X E X X X
        1 X B X X F X X M
        2 X X X X M X M X
        1 C X M X X X X G
        0 X X A X A X X X
        """.trimIndent(),
        """
        44
        MEDIUM
        0 1 0 2 1 2 1 2 3
        1 X X X E F X X M
        1 X D M E D X X F
        1 X D X E M X X X
        2 X X X E C D M M
        2 M X E X M X X X
        1 X X X X X X X M
        3 B X C M A M M A
        1 X X M X X X X X
        """.trimIndent(),
        """
        45
        MEDIUM
        0 0 2 2 3 1 1 2 1
        1 X M D X X X X X
        1 C X E M X X X X
        1 D X M F D X X X
        1 X X X E X X M X
        4 B M C M X G M M
        0 B X X X B X X X
        4 X X M M M M X H
        0 X X X X H X X X
        """.trimIndent(),
        """
        1
        HARD
        0 1 1 1 4 2 0 2 1
        3 D D M M E X M G
        0 X D D X D D F X
        2 X X X M G X M X
        2 X M X E M X X X
        4 M X H M M X X M
        0 X X X H X X X X
        0 X B X X X X H X
        1 X X X M X X H X
        """.trimIndent(),
        """
        2
        HARD
        0 2 2 1 0 1 1 1 4
        3 M E X F M G X M
        0 X X X X X X X X
        1 E M D X F F X X
        1 X B X X X M X E
        1 C X X X D X X M
        2 X X M B H X X M
        1 M X X X X X X X
        3 B M X X G B M M
        """.trimIndent(),
        """
        3
        HARD
        0 1 1 3 3 0 2 1 1
        0 E X D X D X X F
        1 X X X X F M X X
        2 X M X X X G C M
        2 X D M X X M G X
        1 X A X M X X X X
        3 M X C M X X M X
        2 X X M M G X X X
        1 X X M X X X X A
        """.trimIndent(),
        """
        4
        HARD
        0 4 2 3 1 1 0 1 0
        1 X X M X X X E X
        3 M C M G F X M X
        2 M M X X X G F X
        1 X X X M X X X X
        1 X M X X X H H X
        2 M B H X M X H X
        1 C X M B X X H X
        1 M A X X X X A X
        """.trimIndent(),
        """
        5
        HARD
        0 3 2 1 1 0 2 1 2
        1 X X M X X X G E
        2 E M X X X X X M
        2 X X X X X M B M
        1 M X X X X H X X
        1 X X X X H M X X
        0 X X X X X X X X
        3 M M X X X A M A
        2 M C X M H B X A
        """.trimIndent(),
        """
        6
        HARD
        0 2 1 0 2 3 1 1 2
        1 X X X E M X X E
        1 E X X M F X X X
        2 M X G M X X X X
        2 M X X A C X M F
        1 A M X G E H H X
        2 X X X X M X X M
        3 X X X B M M X M
        0 X X X X B X X X
        """.trimIndent(),
        """
        7
        HARD
        0 1 0 1 1 1 4 2 2
        3 X D D X M M E M
        1 X X M D X X X X
        1 X B X X X D M X
        2 M X X X X M X H
        1 B X X X X X X M
        0 X X H X X F X X
        1 X X X X X M X X
        3 C B X M G M M G
        """.trimIndent(),
        """
        8
        HARD
        0 3 3 1 1 0 1 1 2
        1 M X X X X X E X
        0 X X X X X D X X
        1 X M X X X X X X
        5 M M G X X M M M
        3 M M H E X X X M
        0 D X X X F H X X
        1 X X X M X X X X
        1 A A M X X X X X
        """.trimIndent(),
        """
        9
        HARD
        0 3 1 1 1 2 2 1 1
        4 M X X F M M G M
        3 D C X M M M E X
        1 C M X X X X X G
        0 X X X B X X X H
        0 D X X X X X X X
        1 C X X X X X M G
        2 M X M X X X G X
        1 M A X X X X X X
        """.trimIndent(),
        """
        10
        HARD
        0 1 3 1 1 0 2 2 2
        1 X X X X X M X X
        1 X M X X X X X X
        2 X E M B C X M E
        3 C B X M G E M M
        2 M M G X X D X X
        1 X M X X X X X X
        2 B X X C C M X M
        0 X X X X X X H X
        """.trimIndent(),
        """
        11
        HARD
        0 3 1 2 1 0 2 0 3
        5 M M X M X M X M
        1 D X X X F X X M
        0 E X X X X X X F
        1 X X M X H X X G
        3 M X M C G M X X
        1 B B X X X X X M
        0 B X X X X A X X
        1 M X X X X H X X
        """.trimIndent(),
        """
        12
        HARD
        0 2 1 2 1 4 1 0 1
        1 E X X X M X X X
        3 X X F M M M G G
        3 D M M X M X H X
        1 M X B X X X X F
        0 B X X X X X X X
        0 X X F X H X X X
        1 B X X X M X X X
        3 M X M X H X X M
        """.trimIndent(),
        """
        13
        HARD
        0 1 1 2 2 2 1 2 1
        2 M E M G F X E X
        1 X X X M F X X F
        1 X X X X X X M X
        4 D X M X M C M M
        1 X X D M X X X H
        1 X M G X A X X X
        0 X X X X X X X H
        2 X X B B M M X X
        """.trimIndent(),
        """
        14
        HARD
        0 1 2 2 1 0 0 3 3
        1 X E M X X X X X
        1 D B X X X D M X
        2 X X A M G X X M
        2 D M X X G C X M
        0 X X X X X H X X
        3 M E M X G X G M
        2 X M X X X X M G
        1 C X B X X X M X
        """.trimIndent(),
        """
        15
        HARD
        0 2 2 1 0 2 1 2 2
        1 X E X F X M F X
        2 X X M X X X X M
        1 M X X F X A X X
        1 C X X X X X M X
        1 X M X X X X X X
        3 X M X H B X M M
        2 M X G H M X X H
        1 X B X X M X X X
        """.trimIndent(),
        """
        16
        HARD
        0 3 3 1 1 1 2 1 0
        2 M M D X X X X X
        2 M E X X M X X X
        1 X M X D D X F X
        2 A M X X X M H X
        3 E D M X X M M X
        0 X A X H X X X X
        1 X X B M X G X X
        1 M X A B X X X X
        """.trimIndent(),
        """
        17
        HARD
        0 2 0 3 1 1 2 1 2
        1 X D X M F X X E
        3 M X M G X M X G
        1 A X X X X X X M
        0 D X A X X A X X
        2 M X B X X A M H
        0 X X X X H X F X
        3 C X M X X M X M
        2 X X M X M X X A
        """.trimIndent(),
        """
        18
        HARD
        0 0 0 3 1 1 5 2 0
        1 X X E D M X F X
        3 D X M X X M M X
        1 X X E X X M X X
        1 X B X X X M X X
        3 X X M X X M M X
        1 X D M X X X F X
        1 X X X H X M X H
        1 X B A M X A X G
        """.trimIndent(),
        """
        19
        HARD
        0 3 1 1 1 1 2 1 2
        1 M X E F X X X X
        1 E X X X F D M E
        1 D D X M X X X G
        3 M X X H B M X M
        4 M X M F X M X M
        1 C B X X M X X H
        1 A M B B H B X A
        0 X X X X X A X X
        """.trimIndent(),
        """
        20
        HARD
        0 1 2 2 2 2 1 1 1
        0 X X X X X X F X
        2 X E M C E X X M
        2 X X X M C X M X
        5 M M M M M X X H
        1 D X X X M X X F
        1 X M X H X X X X
        1 X A X A A M X X
        0 X X X X X X X X
        """.trimIndent(),
        """
        21
        HARD
        0 1 2 1 1 1 2 2 2
        1 X X X E X M E X
        1 X D X M D X X X
        1 X M X F H X X H
        1 X E X X X D M X
        4 M M X X X D M M
        1 X X X D F M X X
        2 X X X X M X X M
        1 X A M X X G A A
        """.trimIndent(),
        """
        22
        HARD
        0 2 1 1 1 2 1 2 2
        1 X X X M X X F X
        1 X X X X X M X G
        1 M B X X X X H E
        3 M X X X E X M M
        3 X M X X M X M E
        1 X D H X X H F M
        1 C C M X X X A X
        1 X B X X M X A X
        """.trimIndent(),
        """
        23
        HARD
        0 1 1 1 2 1 1 1 4
        4 M C X M M X G M
        1 C X X B F X X M
        1 X X X X X X X M
        1 B D M X X X X X
        0 X X X X X X F X
        0 X X X X X X X A
        3 X M C A X X M M
        2 X C X M A M G X
        """.trimIndent(),
        """
        24
        HARD
        0 1 1 1 1 2 2 2 2
        1 C M D X X X F X
        2 X X H X C X M M
        3 X X X M M X M A
        0 D X X X X X X X
        0 X X X F D E A X
        2 X X M B X M X G
        3 M X X G C M A M
        1 X X X X M X X X
        """.trimIndent(),
        """
        25
        HARD
        0 2 0 1 2 0 2 2 3
        0 X D X X D D X E
        1 X X X X F E M X
        1 X X X X X M X X
        1 X X X E X X X M
        1 X X X M F X X X
        4 M X X M H M X M
        4 M X M A X A M M
        0 B B X X X X X X
        """.trimIndent(),
        """
        26
        HARD
        0 2 1 0 3 2 1 1 2
        2 C X C M X G X M
        0 X X X D X F X X
        1 D X X X M X X X
        1 B X X X X M X X
        1 D M X B X X X A
        1 B X X M G X X X
        1 M X X A X X X X
        5 M X X M M H M M
        """.trimIndent(),
        """
        27
        HARD
        0 1 1 5 1 3 0 0 1
        2 X C M X M X G G
        1 X X M X D X X X
        1 B M A F X X X X
        1 X X M X X F X X
        2 X X M X X H X M
        2 X B X M M X H X
        0 X X X B A X X X
        3 M C M A M X G X
        """.trimIndent(),
        """
        28
        HARD
        0 1 1 5 1 0 0 3 1
        3 M E M M X F E X
        1 C E E X X F M X
        1 X B M X X X X X
        1 D X X X B X M X
        3 X M M X D X M G
        1 X X M X X H X X
        1 X X M X X X X X
        1 X A B X X H X M
        """.trimIndent(),
        """
        29
        HARD
        0 0 1 2 1 2 4 1 1
        2 X X E X M E G M
        1 X D E X F M X X
        1 D C X M X X X X
        1 C X X X X M X G
        2 X C M X X M G X
        0 X X X X X D X H
        5 X M M X M M M X
        0 B X X X X X H X
        """.trimIndent(),
        """
        30
        HARD
        0 1 0 1 3 2 3 1 1
        0 X X X X X X X E
        1 D X X X M X F X
        2 X X X M X X M X
        1 X D X F X M F X
        2 X B M M X X H X
        1 X X X H H M X X
        5 M X X M M M H M
        0 X X X X X X X X
        """.trimIndent(),
        """
        31
        HARD
        0 1 1 3 2 1 1 2 1
        3 M D E X F M E M
        1 D X M D F X X F
        3 X X M X M G M X
        1 B X M X X X X X
        0 X X X X X X X X
        3 X M D M X X M X
        1 B X X M H X X X
        0 X B X X X X A H
        """.trimIndent(),
        """
        32
        HARD
        0 1 2 1 2 0 0 5 1
        1 E E X M X X X E
        3 X M M X G X M X
        3 X D X M X X M M
        0 D F X H H X X X
        2 M M X X X X X H
        1 X A X X X X M X
        1 X A X X X X M X
        1 A X B X X B M X
        """.trimIndent(),
        """
        33
        HARD
        0 2 3 2 0 2 0 1 2
        2 D M X X X X M X
        1 E X M X X X X F
        1 M E D H X X X X
        4 C M M B M G X M
        1 X X X F M X X X
        1 D X X X X X X M
        1 B M X X X X B H
        1 M X X X X X X X
        """.trimIndent(),
        """
        34
        HARD
        0 1 2 2 1 2 1 2 1
        4 X M E C M E M M
        2 D M C C X X M X
        1 B X M X X X X F
        1 X X M X A X X X
        0 X X D X D F X X
        1 X X F X X M X X
        2 X X X M M X X X
        1 M A A X X X X X
        """.trimIndent(),
        """
        35
        HARD
        0 2 2 1 1 1 1 1 3
        2 X M X C G F X M
        0 X X X D X X X F
        2 D X X H M X M X
        3 M X M X D X X M
        2 C M X H B M X G
        1 X X X M X X X X
        0 B X X X X X X X
        2 M X X X G X G M
        """.trimIndent(),
        """
        36
        HARD
        0 1 2 1 3 3 0 2 0
        1 X X F X M X X X
        1 X X X M E X X X
        2 M X M E F X E F
        1 X X F M X F X X
        1 X M B X X X X X
        0 X A X X X X E X
        3 X X X M M X M X
        3 X M X C M H M X
        """.trimIndent(),
        """
        37
        HARD
        0 2 1 1 1 1 2 1 3
        0 X X F D X X X X
        3 X C M X M F M F
        1 M X F X D X X X
        3 X X C M X M X M
        1 M X X X X X X E
        1 X X X X X X X M
        2 X X X X X M H M
        1 X M A H X G X X
        """.trimIndent(),
        """
        38
        HARD
        0 3 1 1 1 0 1 1 4
        2 M D X X X X X M
        1 M X X X X X X G
        1 X X D X X X X M
        2 X M X M X X X G
        0 B X X H F X X F
        1 X X X C B H M X
        2 C X M X X X X M
        3 M X X H X M A M
        """.trimIndent(),
        """
        39
        HARD
        0 1 0 2 3 0 1 3 2
        1 X X D X X E M X
        1 X X M D X X E X
        3 X X M F D G M M
        1 X X X M X X X X
        0 X X X X D X X A
        4 M X X M B M H M
        0 B X X X X X X X
        2 X X X M X X M X
        """.trimIndent(),
        """
        40
        HARD
        0 2 1 3 1 3 1 1 0
        1 X E F X M F X X
        1 X M X X F X X X
        2 M X X M X X X F
        1 X X M X H X X F
        0 X X X X H X X F
        1 X X M H H X X X
        1 X X A X M X X X
        5 M A M G M M M X
        """.trimIndent(),
        """
        41
        HARD
        0 3 2 1 2 0 1 3 0
        2 M E X X X X M X
        1 X X M X X X X X
        1 E B H D X X M X
        3 M M X M X F G X
        3 M A B M X M G X
        1 A X X X H X M G
        0 X X X X X X X X
        1 B M X X X X X X
        """.trimIndent(),
        """
        42
        HARD
        0 1 4 1 3 0 1 1 1
        1 C M X X D X X G
        1 C X H M X X X X
        0 X X X X X X X X
        3 M A D C X M X M
        1 X M X X X X X G
        1 B M H X F X X X
        1 B C X M X X X H
        4 C M M M X X M G
        """.trimIndent(),
        """
        43
        HARD
        0 2 2 0 1 2 2 0 3
        3 M M X X X G X M
        1 D X X X F X X M
        1 D X X X X M X X
        1 X X X X X A F M
        1 X M X X X G G G
        2 B X H X M M X G
        3 M X X M M A X H
        0 A B X X X X X X
        """.trimIndent(),
        """
        44
        HARD
        0 2 3 1 2 2 0 2 0
        1 X M E E X X X X
        3 M E D M X G M X
        1 M D X X X X X X
        3 D C M X M X M X
        1 X M X X X X X F
        0 X D B H X X X X
        1 X M X X A X X X
        2 X X X M M X H X
        """.trimIndent(),
        """
        45
        HARD
        0 4 0 1 2 3 1 0 1
        2 D C E X M M X X
        1 M D X X X X X X
        2 E X M G M X X X
        0 X D X X X F X X
        1 M X X X X X X F
        1 X B X M X H X G
        1 M X X X X X G G
        4 M X X M M A H M
        """.trimIndent()
        )
    }

}