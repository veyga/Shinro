package io.astefanich.shinro.database

import androidx.room.*
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Difficulty
import io.astefanich.shinro.domain.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM game_table WHERE id = 1")
    fun getActiveGame(): Game

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: Game)

    @Update
    fun updateGame(game: Game)

}

@Dao
interface BoardDao {

    @Query("SELECT * FROM board_table WHERE board_num = :boardNum AND difficulty = :difficulty")
    fun getBoardByNumAndDifficulty(boardNum: Int, difficulty: Difficulty): Board

    @Insert
    fun insertBoards(vararg boards: Board)

//    @Update
//    fun updateBoard(board: Board)
}
