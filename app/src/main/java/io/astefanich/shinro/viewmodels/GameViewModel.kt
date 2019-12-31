package io.astefanich.shinro.viewmodels

import android.widget.Toast
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

    fun onMove(row: Int, column: Int) {
        Timber.i("Moving. marbled placed increase by one")
        val cell = _board.grid.cells[row][column]

        //if user clicks on arrow do nothing
        if (cell.actual in "A".."G") {
            Timber.i("clicked on arrow. ignoring move")
            return
        }

        if (cell.current == " ") {
            cell.current = "X"
        } else if (cell.current == "M") {
            cell.current = " "
            _board.marblesPlaced -= 1
            if (gameWon()) //can win by placing marbles or taking them away
                Timber.i("YOU WON!!!!!")
        } else {
            cell.current = "M"
            _board.marblesPlaced += 1
            if (_board.marblesPlaced > 12) {
                Timber.i("Too Many Marbles Placed (12 required)")
            } else if (gameWon()) {
                Timber.i("YOU WON!!!!!")
            }
        }
        board.value = _board
    }

    fun gameWon(): Boolean {
        if (_board.marblesPlaced != 12)
            return false

        val cells = _board.grid.cells
        for (i in 0..8) {
            for (j in 0..8) {
                val cell = cells[i][j]
                if (cell.actual == "M" && cell.current != "M")
                    return false
            }
        }
        return true
    }

//    fun getCurrentCellValue(row: Int, column: Int): String {
//        return _board.grid.cells[row][column].current
//    }
}