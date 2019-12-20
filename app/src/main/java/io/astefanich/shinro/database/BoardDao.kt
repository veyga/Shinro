package io.astefanich.shinro.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.astefanich.shinro.domain.Board

@Dao
interface BoardDao {

    @Query("SELECT * FROM board_table WHERE board_id = :boardId")
    fun getBoardById(boardId: Int): LiveData<Board>

    @Query("SELECT * FROM board_table")
    fun getAllBoards(): LiveData<List<Board>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBoards(vararg boards: Board)

    @Insert
    fun insertOneBoard(board: Board)
}