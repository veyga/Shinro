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


// Coroutines go here (not the VM?)
@Singleton
class BoardRepositoryImpl @Inject constructor(val boardDao: BoardDao) : BoardRepository {

    private val boards: LiveData<List<Board>>


    init {
        Timber.i("inserted test board")
        boardDao.insertBoards(Board(-1, "TEST"))
        boards = boardDao.getAllBoards()
    }

    override fun updateBoard(board: Board) {
        Timber.i("fake repo updating")
    }

    override fun getAllBoards(): LiveData<List<Board>> {
        return boards
    }

    override fun getBoardById(boardId: Int): LiveData<Board> {
        Timber.i("repo getting board by id")
        //get it the board from the repos list
        return boardDao.getBoardById(boardId)
    }

    override fun insertBoards(vararg boards: Board) {
        boardDao.insertBoards(*boards)
    }
}
