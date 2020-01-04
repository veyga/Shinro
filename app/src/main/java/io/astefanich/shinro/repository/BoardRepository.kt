package io.astefanich.shinro.repository

import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import javax.inject.Inject


class BoardRepository @Inject constructor(val boardDao: BoardDao) {

    fun getBoardById(boardId: Int): Board? = boardDao.getBoardById(boardId)

    fun getLowestIncompleteBoard(): Board = boardDao.getLowestIncompleteBoard()

    fun insertBoards(vararg boards: Board) = boardDao.insertBoards(*boards)

    fun updateBoard(board: Board) = boardDao.updateBoard(board)
}
