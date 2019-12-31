package io.astefanich.shinro.repository

import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Cell
import io.astefanich.shinro.domain.Grid
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


// Coroutines go here (not the VM?)
@Singleton
class BoardRepositoryImpl @Inject constructor(val boardDao: BoardDao) : BoardRepository {


    init {
        Timber.i("inserted test board")
        val testBoard = Board(
            -1, "EASY", Grid(
                arrayOf(
                    arrayOf(Cell('M'), Cell('F')),
                    arrayOf(Cell('M'), Cell('F'))
                )
            )
        )
        boardDao.insertBoards(testBoard)
    }

    override fun updateBoard(board: Board) {
       boardDao.updateBoard(board)
    }


    override fun getBoardById(boardId: Int): Board {
        Timber.i("repo getting board by id: $boardId")
        //get it the board from the repos list
        return boardDao.getBoardById(boardId)
    }

    override fun insertBoards(vararg boards: Board) {
        boardDao.insertBoards(*boards)
    }
}
