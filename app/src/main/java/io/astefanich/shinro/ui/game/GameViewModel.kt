package io.astefanich.shinro.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Cell
import io.astefanich.shinro.domain.Difficulty

class GameViewModel(boardId: Int) : ViewModel() {


    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board>
        get() = _board

    init {
        _board.value = getBoardById(boardId)
    }


    internal fun nextPuzzle(){

    }

    private fun getBoardById(boardId: Int): Board {
        val boards = arrayOf(
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
            ), Board(
                2, Difficulty.MEDIUM,
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
            ), Board(
                3, Difficulty.HARD,
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
        )

        if (boardId == 0)
            return boards[0]
        else
            return boards[boardId-1]
    }
}