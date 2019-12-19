package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeBoardRepository @Inject constructor(val dao: BoardDao) : BoardRepository {

    override fun getBoardById(boardId: Int): LiveData<Board> = dao.getBoardById(boardId)
//    override fun getBoardById(boardId: Int): Board {
//        val boards = arrayOf(
//            Board(1, Difficulty.EASY),
//            Board(2, Difficulty.MEDIUM),
//            Board(3, Difficulty.HARD)
//        )
//
//        //if boardId is 0, user is coming from title screen.
//        //return lowest incomplete board
//        return if (boardId == 0)
//            boards[0]
//        else
//            boards[boardId - 1]
//    }
}