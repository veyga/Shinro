package io.astefanich.shinro.database

import androidx.room.*
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.model.Board
import io.astefanich.shinro.model.Game
import io.astefanich.shinro.model.GameResult
import java.util.*

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

    @Insert
    fun insertBoards(vararg boards: Board)

}

@Dao
interface BlacklistDao {

    @Query("SELECT reserved FROM blacklist_table WHERE difficulty = :difficulty ")
    suspend fun getBlacklistByDifficulty(difficulty: Difficulty): Queue<Int>?
}

