package io.astefanich.shinro.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Progress
import timber.log.Timber
import java.io.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named


class BoardRepository @Inject constructor(
    val boardDao: BoardDao,
    val mContext: Context,
    @Named("lastVisitedFile") val lastVisitedFileName: String
) {

//    private val _progressList = MutableLiveData<List<Progress>>()
//    val getProgress: LiveData<List<Progress>>
//        get() = _progressList
//
//    init {
//        _progressList.value = mutableListOf(
//            Progress("easy", true),
//            Progress("easy", false)
//        )
//    }


    fun getBoardById(boardId: Int): Board = boardDao.getBoardById(boardId)

    fun insertBoards(vararg boards: Board) = boardDao.insertBoards(*boards)

    fun updateBoard(board: Board) = boardDao.updateBoard(board)

    fun getProgress(): LiveData<List<Progress>> = boardDao.getProgress()

    fun updateLastViewedBoardId(id: Int) {
        val fileOut = mContext.openFileOutput(lastVisitedFileName, Context.MODE_PRIVATE)
        val writer = fileOut.writer(Charsets.UTF_8)
        writer.use { it.write("$id") }
    }

    fun getLastViewedBoardId(): Int {
        return try {
            val fileIn = InputStreamReader(mContext.openFileInput(lastVisitedFileName))
            val lastVisitedId = fileIn.readLines()[0]
            Integer.parseInt(lastVisitedId)
        } catch (e: Exception) {
            1 //file not yet created, start from board 1
        }
    }
}
