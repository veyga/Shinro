package io.astefanich.shinro.repository

import io.astefanich.shinro.domain.Board

interface BoardRepository {


    fun getBoardById(boardId: Int): Board

    fun getLowestIncompleteBoard(): Board

    fun insertBoards(vararg boards: Board)

    fun updateBoard(board: Board)

}
