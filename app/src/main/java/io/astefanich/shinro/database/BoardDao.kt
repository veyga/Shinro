package io.astefanich.shinro.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.astefanich.shinro.domain.Board

@Dao
interface BoardDao {

    @Query("SELECT * FROM board_table WHERE board_id = :boardId")
    fun getBoardById(boardId: Int): Board


    @Insert
    fun insertBoards(vararg boards: Board)

    @Update
    fun updateBoard(board: Board)
}