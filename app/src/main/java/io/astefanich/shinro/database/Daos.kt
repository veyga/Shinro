package io.astefanich.shinro.database

import androidx.room.*
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.model.Board
import io.astefanich.shinro.model.Game
import io.astefanich.shinro.model.ResultAggregate

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

    @Query("SELECT * FROM results_table WHERE difficulty = :difficulty")
    suspend fun getAggregateByDifficulty(difficulty: Difficulty): ResultAggregate

    @Insert
    suspend fun insertAggregates(vararg aggregates: ResultAggregate)

    @Update
    suspend fun updateAggregate(aggregate: ResultAggregate)
}

@Dao
interface BoardDao {

    @Query("SELECT * FROM board_table WHERE board_num = :boardNum AND difficulty = :difficulty")
    suspend fun getBoardByNumAndDifficulty(boardNum: Int, difficulty: Difficulty): Board

    //inserted on db create
    @Insert
    suspend fun insertBoards(vararg boards: Board)

}

