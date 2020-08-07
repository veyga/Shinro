package io.astefanich.shinro.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.ProgressItem

@Dao
interface BoardDao {

    @Query("SELECT * FROM board_table WHERE board_id = :boardId")
    fun getBoardById(boardId: Int): Board

    @Query("SELECT board_id, difficulty, completed FROM board_table")
    fun getProgress(): List<ProgressItem>

    @Insert
    fun insertBoards(vararg boards: Board)

    @Update
    fun updateBoard(board: Board)
}