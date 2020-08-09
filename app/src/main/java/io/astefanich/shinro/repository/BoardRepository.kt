package io.astefanich.shinro.repository

import android.content.Context
import androidx.lifecycle.LiveData
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Progress
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Named


class BoardRepository @Inject constructor(
    val boardDao: BoardDao,
    val ctx: Context,
    @Named("lastVisitedFile") val lastVisitedFileName: String
) {

    fun getBoardById(boardId: Int): Board = boardDao.getBoardById(boardId)

    fun insertBoards(vararg boards: Board) = boardDao.insertBoards(*boards)

    fun updateBoard(board: Board) = boardDao.updateBoard(board)

    fun getProgress(): LiveData<List<Progress>> = boardDao.getProgress()

    fun updateLastViewedBoardId(id: Int) {
        val fileOut = ctx.openFileOutput(lastVisitedFileName, Context.MODE_PRIVATE)
        val writer = fileOut.writer(Charsets.UTF_8)
        writer.use { it.write("$id") }
    }

    fun getLastViewedBoardId(): Int {
        return try {
            val fileIn = InputStreamReader(ctx.openFileInput(lastVisitedFileName))
            val lastVisitedId = fileIn.readLines()[0]
            Integer.parseInt(lastVisitedId)
        } catch (e: Exception) {
            1 //file not yet created, start from board 1
        }
    }
}
