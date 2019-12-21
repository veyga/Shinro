package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import io.astefanich.shinro.domain.Board

interface BoardRepository {

    fun getAllBoards(): LiveData<List<Board>>

    fun getBoardById(boardId: Int): LiveData<Board>

    fun insertBoards(vararg boards: Board)

    fun updateBoard(board: Board)

}
