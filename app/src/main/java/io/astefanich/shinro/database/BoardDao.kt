package io.astefanich.shinro.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.astefanich.shinro.domain.Board

@Dao
interface BoardDao {

    @Query("SELECT * FROM board_table WHERE board_id = :boardId")
    fun getBoardById(boardId: Int): LiveData<Board>


    @Query("SELECT * FROM board_table ORDER BY board_id ASC")
    fun getAllBoards(): LiveData<List<Board>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBoards(vararg boards: Board)

    @Update
    fun updateBoard(board: Board)
}