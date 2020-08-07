package io.astefanich.shinro.repository

import android.content.Context
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
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

    fun getBoardById(boardId: Int): Board = boardDao.getBoardById(boardId)

    fun insertBoards(vararg boards: Board) = boardDao.insertBoards(*boards)

    fun updateBoard(board: Board) = boardDao.updateBoard(board)

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
