package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import io.astefanich.shinro.domain.Board
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeBoardRepository @Inject constructor() : BoardRepository {


    @Inject
    lateinit var difficulties: Triple<String, String, String>

    override fun getBoardById(boardId: Int): LiveData<Board> {

        val boards = arrayOf(
            Board(1, difficulties.first),
            Board(2, difficulties.second),
            Board(3, difficulties.third)
        )

        //if boardId is 0, user is coming from title screen.
        //return lowest incomplete board
        return if (boardId == 0)
            boards[0] as LiveData<Board>
        else
            boards[boardId - 1] as LiveData<Board>
    }
}