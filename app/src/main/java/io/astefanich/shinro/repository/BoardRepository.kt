package io.astefanich.shinro.repository

import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.database.BoardHistoryDao
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.model.Board
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class BoardRepository
@Inject
constructor(
    val boardDao: BoardDao,
    val boardHistoryDao: BoardHistoryDao
) {

//    private val randomBoardIdGen = { Random.nextInt(1, 46) }

    suspend fun getRandomBoardByDifficulty(difficulty: Difficulty): Board {
        //TODO select random number, assert not in blacklist
//        var targetBoard = randomBoardIdGen() //use 1 for testing
//        var history = boardHistoryDao.getBoardHistoryByDifficulty(difficulty)
//        if(history == null) {
//            delay(1000)
//        }
//        history = boardHistoryDao.getBoardHistoryByDifficulty(difficulty)
//        val blacklist = history.blacklist
//        while (blacklist.contains(targetBoard)) {
//            Timber.i("$targetBoard is blacklisted. continuing..")
//            targetBoard = randomBoardIdGen()
//        }
//        Timber.i("$targetBoard is NOT blacklisted. adding to blacklist")
//        if (blacklist.size >= 3) { //arraydeque default == 16
//            val whitelisted = blacklist.remove()
//            Timber.i("blacklist full. adding $whitelisted to whitelist")
//        }
//        blacklist.add(targetBoard)
//        boardHistoryDao.updateBoardHistory(history)
        var newBoard: Board? = null
        withContext(Dispatchers.IO){
            delay(500) //RACE CONDTION during inMemoryDB. boards need to be loaded first
            newBoard = boardDao.getBoardByNumAndDifficulty(1, difficulty)
            Timber.i("board repo serving up board#: ${newBoard!!.boardNum} diff: ${newBoard!!.difficulty}. Thread: ${Thread.currentThread()}")
        }
        return newBoard!!
    }


//    fun getBoardById(boardId: Int): Board = boardDao.getBoardById(boardId)
//
//    fun insertBoards(vararg boards: Board) = boardDao.insertBoards(*boards)
//
//    fun updateBoard(board: Board) = boardDao.updateBoard(board)
//
//    fun updateLastViewedBoardId(id: Int) {
//        val fileOut = ctx.openFileOutput(lastVisitedFileName, Context.MODE_PRIVATE)
//        val writer = fileOut.writer(Charsets.UTF_8)
//        writer.use { it.write("$id") }
//    }
//
//    fun getLastViewedBoardId(): Int {
//        return try {
//            val fileIn = InputStreamReader(ctx.openFileInput(lastVisitedFileName))
//            val lastVisitedId = fileIn.readLines()[0]
//            Integer.parseInt(lastVisitedId)
//        } catch (e: Exception) {
//            1 //file not yet created, start from board 1
//        }
//    }
}
