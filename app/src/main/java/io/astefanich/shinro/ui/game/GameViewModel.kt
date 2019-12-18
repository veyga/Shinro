package io.astefanich.shinro.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository

class GameViewModel(boardId: Int) : ViewModel() {


    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board>
        get() = _board

    private val repository = BoardRepository()

    init {
        _board.value = repository.getBoardById(boardId)
    }


}