package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardRepositoryImpl @Inject constructor(val boardDao: BoardDao) : BoardRepository {

    val boards: LiveData<List<Board>> = boardDao.getAllBoards()

    init {
        Timber.i("real repo init" )
    }

    override fun insertBoards(vararg boards: Board) {
        Timber.i("real board repo inserting boards")
        Timber.i("${Arrays.toString(boards)}")
        boardDao.insertBoards(*boards)
    }

    override fun getAllBoards(): LiveData<List<Board>> {
        Timber.i("getting all boards from repo. repo boards are $1")
        if (boards == null) {
            Timber.i("repo boards was null. getting from dao")
            boardDao.getAllBoards()
        }


        return boards
    }

    override fun getBoardById(boardId: Int): LiveData<Board> {
        Timber.i("repo getting board by id")

        return boardDao.getBoardById(boardId)
    }

    override fun insertOneBoard(board: Board) {
        boardDao.insertOneBoard(board)
    }
}
