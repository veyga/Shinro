package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.astefanich.shinro.domain.Board
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeBoardRepository @Inject constructor(val boards: Array<Board>) : BoardRepository {


    override fun getBoardById(boardId: Int): LiveData<Board> {


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