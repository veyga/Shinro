package io.astefanich.shinro.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Cell
import io.astefanich.shinro.domain.Difficulty

class GameViewModel : ViewModel() {

    private val _marblesRemaining = MutableLiveData<Int>()
    val marblesRemaining: LiveData<Int>
        get() = _marblesRemaining

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board>
        get() = _board


    fun solvedStateStr(): String = if (_board.value!!.isSolved) "COMPLETE" else "INCOMPLETE"

    init {
        _marblesRemaining.value = 10
        _board.value =
            Board(
                1, Difficulty.EASY,
                arrayOf(
                    //row 0
                    arrayOf(
                        Cell('j', 'j'),
                        Cell('1', '1'),
                        Cell('2', '2'),
                        Cell('1', '1'),
                        Cell('1', '1'),
                        Cell('1', '1'),
                        Cell('3', '3'),
                        Cell('2', '2'),
                        Cell('1', '1')
                    ),
                    arrayOf(
                        Cell('2', '2'),
                        Cell('O', 'M'),
                        Cell('O', 'O'),
                        Cell('C', 'C'),
                        Cell('E', 'E'),
                        Cell('E', 'E'),
                        Cell('O', 'M'),
                        Cell('O', 'O'),
                        Cell('O', 'O')
                    )
                )
            )
    }

}