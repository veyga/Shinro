package io.astefanich.shinro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import timber.log.Timber
import javax.inject.Inject

class GameViewModel @Inject constructor(val repository: BoardRepository) :
    ViewModel() {

//    private val boardId = 1

    //instance for game logic
    private val _board: Board

    //observable type for board
    val board = MutableLiveData<Board>()


    init {
        Timber.i("game viewmodel created")
        Timber.i("game repo is null? ${repository == null}")
        _board = repository.getBoardById(1)
        board.value = _board
    }

    fun onMove() {
        Timber.i("Moving. marbled placed increase by one")
        _board.marblesPlaced += 1
        board.value = _board
    }

}