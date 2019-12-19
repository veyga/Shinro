package io.astefanich.shinro.repository

import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Difficulty
import javax.inject.Inject

class BoardRepository @Inject constructor() {

    fun getBoardById(boardId: Int): Board {
        val boards = arrayOf(
            Board(1, Difficulty.EASY),
            Board(2, Difficulty.MEDIUM),
            Board(3, Difficulty.HARD)
        )

        if (boardId == 0)
            return boards[0]
        else
            return boards[boardId - 1]
    }

//    val board1 = Board(
//        1, Difficulty.EASY,
//        arrayOf(
//            //row 0
//            arrayOf(
//                Cell('j', 'j'),
//                Cell('1', '1'),
//                Cell('2', '2'),
//                Cell('1', '1'),
//                Cell('1', '1'),
//                Cell('1', '1'),
//                Cell('3', '3'),
//                Cell('2', '2'),
//                Cell('1', '1')
//            ),
//            arrayOf(
//                Cell('2', '2'),
//                Cell('O', 'M'),
//                Cell('O', 'O'),
//                Cell('C', 'C'),
//                Cell('E', 'E'),
//                Cell('E', 'E'),
//                Cell('O', 'M'),
//                Cell('O', 'O'),
//                Cell('O', 'O')
//            )
}