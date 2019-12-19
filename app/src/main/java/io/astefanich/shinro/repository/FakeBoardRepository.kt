package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Difficulty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeBoardRepository @Inject constructor() : BoardRepository {

    override fun getBoardById(boardId: Int): LiveData<Board> {
        val boards = arrayOf(
            Board(1, Difficulty.EASY),
            Board(2, Difficulty.MEDIUM),
            Board(3, Difficulty.HARD)
        )

        //if boardId is 0, user is coming from title screen.
        //return lowest incomplete board
        return if (boardId == 0)
            boards[0] as LiveData<Board>
        else
            boards[boardId - 1] as LiveData<Board>
    }
}