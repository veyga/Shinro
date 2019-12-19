package io.astefanich.shinro.repository

import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Difficulty
import javax.inject.Inject

class FakeBoardRepository @Inject constructor(): BoardRepository {

    override fun getBoardById(boardId: Int): Board {
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
}