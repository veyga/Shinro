package io.astefanich.shinro.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import javax.inject.Inject

class GameViewModel @Inject constructor(val repository: BoardRepository, val boardId: Int) :
    ViewModel() {


    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board>
        get() = _board


//    @Inject
//    lateinit var repository: BoardRepository


    init {
        _board.value = repository.getBoardById(boardId)
    }


}