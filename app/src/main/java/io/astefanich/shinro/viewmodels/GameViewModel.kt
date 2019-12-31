package io.astefanich.shinro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import timber.log.Timber
import javax.inject.Inject

class GameViewModel @Inject constructor(val repository: BoardRepository) :
    ViewModel() {

    var boardId: Int = 0

    //instance for game logic
    lateinit var _board: Board

    //observable type for board
    val board = MutableLiveData<Board>()


    //Used for passing boardId from safeargs to vm.
    //dagger factory makes this difficult
    fun load() {
        Timber.i("viewmodel boardId is $boardId")
        _board = repository.getBoardById(boardId)
        board.value = _board
    }

    fun onMove() {
        Timber.i("Moving. marbled placed increase by one")
        _board.marblesPlaced += 1
        board.value = _board
    }

    fun getCurrentCellValue(row: Int, column: Int): String {
        return _board.grid.cells[row][column].current
    }
}