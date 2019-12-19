package io.astefanich.shinro.repository

import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Difficulty
import javax.inject.Inject

class FakeBoardRepository @Inject constructor() : BoardRepository {

    override fun getBoardById(boardId: Int): Board {
        val boards = arrayOf(
            Board(1, Difficulty.EASY),
            Board(2, Difficulty.MEDIUM),
            Board(3, Difficulty.HARD)
        )

        return if (boardId == 0)
            boards[0]
        else
            boards[boardId - 1]
    }
}