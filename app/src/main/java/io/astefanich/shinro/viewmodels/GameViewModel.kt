package io.astefanich.shinro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import timber.log.Timber
import javax.inject.Inject

class GameViewModel @Inject constructor(val repository: BoardRepository) : ViewModel() {

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

    fun onMove(row: Int, column: Int) {
        val cell = _board.grid.cells[row][column]

        //do nothing if board is already complete or user clicks on arrow
        if (_board.completed || cell.actual in "A".."G")
            return

        fun OClicked() {
            cell.current = "X"
        }

        fun MClicked() {
            cell.current = " "
            _board.marblesPlaced -= 1 //can win by placing marbles or taking them away
            if (_board.marblesPlaced == 12)
                checkWin()
        }

        fun XClicked() {
            cell.current = "M"
            _board.marblesPlaced += 1
            val mPlaced = _board.marblesPlaced
            if (mPlaced == 12)
                checkWin()
            else if (mPlaced > 12)
                Timber.i("You have placed ${mPlaced} marbles, which is too many")

        }

        when (cell.current) {
            " " -> OClicked()
            "M" -> MClicked()
            "X" -> XClicked()
        }
        board.value = _board
    }


    private fun checkWin() {
        var numIncorrect = 0
        val cells = _board.grid.cells
        for (i in 0..8) {
            for (j in 0..8) {
                val cell = cells[i][j]
                if (cell.current == "M" && cell.actual != "M")
                    numIncorrect += 1
            }
        }
        if (numIncorrect == 0) {
            Timber.i("YOU WON!!!!")
            _board.completed = true
        } else
            Timber.i("$numIncorrect of your marbles are in the wrong spots.")
    }

}