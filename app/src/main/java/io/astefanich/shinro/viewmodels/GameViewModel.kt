package io.astefanich.shinro.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import javax.inject.Inject

class GameViewModel @Inject constructor(val repository: BoardRepository, val context: Context) :
    ViewModel() {

    var boardId: Int = 0

    //instance for game logic
    lateinit var _board: Board

    //observable type for board
    val board = MutableLiveData<Board>()


    //Used for passing boardId from safeargs to vm (dagger vmfactory makes this difficult)
    fun load() {
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
                toastIt("You have placed ${mPlaced} marbles, which is too many")

        }

        when (cell.current) {
            " " -> OClicked()
            "M" -> MClicked()
            "X" -> XClicked()
        }
        updateUI()
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
            toastIt("YOU WON!!!!")
            _board.completed = true
            repository.updateBoard(_board)
        } else
            toastIt("$numIncorrect of your marbles are in the wrong spots.")
    }

    fun onReset() {
        val cells = _board.grid.cells
        for (i in 0..8) {
            for (j in 0..8) {
                val cell = cells[i][j]
                if (cell.actual == "M" || cell.actual == "X")
                    cell.current = " "
            }
        }
        _board.completed = false
        _board.marblesPlaced = 0
        toastIt("Cleared")
        updateUI()
    }

    //implement board.observerForever instead?
    private fun updateUI() {
        board.value = _board //setting board value notifies registered observers
    }

    private fun toastIt(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

}