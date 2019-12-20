package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardRepositoryImpl @Inject constructor(val boardDao: BoardDao) : BoardRepository {



    override fun getAllBoards():LiveData<List<Board>>{
        return boardDao.getAllBoards()
    }
    override fun getBoardById(boardId: Int): LiveData<Board> {
        Timber.i("repo getting board by id")

        return boardDao.getBoardById(boardId)
    }

}
