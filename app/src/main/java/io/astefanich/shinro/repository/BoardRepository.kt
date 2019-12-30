package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import io.astefanich.shinro.domain.Board

interface BoardRepository {


    fun getBoardById(boardId: Int): Board

    fun insertBoards(vararg boards: Board)

    fun updateBoard(board: Board)

}
