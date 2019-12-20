package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.astefanich.shinro.domain.Board
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeBoardRepository (val difficulties:Triple<String,String,String>) : BoardRepository {


    override fun getBoardById(boardId: Int): LiveData<Board> {

        val boards = arrayOf(
            Board(1, difficulties.first),
            Board(2, difficulties.second),
            Board(3, difficulties.third)
        )

        //if boardId is 0, user is coming from title screen.
        //return lowest incomplete board
        val item = MutableLiveData<Board>()
        if (boardId == 0)
            item.value = boards[0]
        else
            item.value = boards[boardId - 1]

        return item
    }
}