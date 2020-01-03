package io.astefanich.shinro.repository

import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class BoardRepository @Inject constructor(val boardDao: BoardDao) {

    suspend fun getBoardById(boardId: Int): Board? = withContext(Dispatchers.IO) {
        boardDao.getBoardById(boardId)
    }

    suspend fun getLowestIncompleteBoard(): Board = withContext(Dispatchers.IO) {
        boardDao.getLowestIncompleteBoard()
    }

    suspend fun insertBoards(vararg boards: Board) = withContext(Dispatchers.IO) {
        boardDao.insertBoards(*boards)
    }

    suspend fun updateBoard(board: Board) = withContext(Dispatchers.IO) {
        boardDao.updateBoard(board)
    }

}
