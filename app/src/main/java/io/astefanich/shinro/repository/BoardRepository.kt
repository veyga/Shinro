package io.astefanich.shinro.repository

import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import timber.log.Timber
import javax.inject.Inject


class BoardRepository @Inject constructor(val boardDao: BoardDao, dummyBoard: Board) {


    fun getBoardById(boardId: Int): Board? {
        Timber.i("repo getting boardId: $boardId")
        val result = boardDao.getBoardById(boardId)
        Timber.i("board got is $result")
        return result
    }

    fun getLowestIncompleteBoard(): Board = boardDao.getLowestIncompleteBoard()

    fun insertBoards(vararg boards: Board) {
        Timber.i("repo inserting board $boards}")
        return boardDao.insertBoards(*boards)
    }

    fun updateBoard(board: Board) = boardDao.updateBoard(board)
}
