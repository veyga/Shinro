package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardRepositoryImpl @Inject constructor(val boardDao: BoardDao) : BoardRepository {

    override fun getBoardById(boardId: Int): LiveData<Board> = boardDao.getBoardById(boardId)

}
