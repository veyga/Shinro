package io.astefanich.shinro.database

import androidx.room.*
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.model.BoardHistory
import io.astefanich.shinro.model.Board
import io.astefanich.shinro.model.Game
import io.astefanich.shinro.model.GameResult

@Dao
interface GameDao {

    @Query("SELECT * FROM game_table WHERE id = 1")
    suspend fun getActiveGame(): Game

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: Game)

    @Update
    suspend fun updateGame(game: Game)

}

@Dao
interface ResultsDao {

    @Insert
    suspend fun insertGameResult(result: GameResult)
}

@Dao
interface BoardDao {

    @Query("SELECT * FROM board_table WHERE board_num = :boardNum AND difficulty = :difficulty")
    suspend fun getBoardByNumAndDifficulty(boardNum: Int, difficulty: Difficulty): Board

    //inserted on db create
    @Insert
    suspend fun insertBoards(vararg boards: Board)

}

@Dao
interface BoardHistoryDao {

    @Query("SELECT * FROM board_history_table WHERE difficulty = :difficulty ")
    suspend fun getBoardHistoryByDifficulty(difficulty: Difficulty): BoardHistory

    @Update
    suspend fun updateBoardHistory(blackList: BoardHistory)

    //inserted on db create
    @Insert
    suspend fun insertBoardHistory(blackList: BoardHistory)
}

