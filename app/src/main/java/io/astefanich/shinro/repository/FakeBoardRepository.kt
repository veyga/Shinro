package io.astefanich.shinro.repository

import io.astefanich.shinro.domain.Board
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeBoardRepository @Inject constructor(val boards: Array<Board>) : BoardRepository {

    override fun insertBoards(vararg boards: Board) {
        Timber.i("fake repo loaded one board")
    }


    override fun getLowestIncompleteBoard(): Board = TODO()

    override fun updateBoard(board: Board) {
        Timber.i("fake repo updating")
    }



    override fun getBoardById(boardId: Int): Board {

        //if boardId is 0, user is coming from title screen.
        //return lowest incomplete board
        return if(boardId == 0)
            boards[0]
        else
            boards[boardId -1]
    }
}