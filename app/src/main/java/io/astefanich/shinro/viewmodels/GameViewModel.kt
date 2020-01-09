package io.astefanich.shinro.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import java.util.*
import javax.inject.Inject

private class Move(val row: Int, val column: Int, val oldVal: String, val newVal: String)

/**
 * Core game logic class
 */
class GameViewModel @Inject constructor(
    val repo: BoardRepository,
    var boardId: Int,
    val context: Context
) : ViewModel() {


    val board = MutableLiveData<Board>()
    val undoStackActive = MutableLiveData<Boolean>()
    private val _board: Board
    private var undoStack = Stack<Move>()

    init {
        //boardId == 0 -> user is coming from title fragment
        _board = if (boardId == 0) repo.getLowestIncompleteBoard() else repo.getBoardById(boardId)
        boardId = _board.boardId
        board.value = _board
        undoStackActive.value = false
    }


    fun onMove(row: Int, column: Int) {
        val cell = _board.grid.cells[row][column]

        //do nothing if board is already complete or user clicks on arrow
        if (_board.completed || cell.actual in "A".."G")
            return

        undoStackActive.value = true

        fun OClicked() {
            undoStack.push(Move(row, column, " ", "X"))
            cell.current = "X"
        }

        fun MClicked() {
            undoStack.push(Move(row, column, "M", " "))
            cell.current = " "
            _board.marblesPlaced -= 1 //can win by placing marbles or taking them away
            if (_board.marblesPlaced == 12)
                checkWin()
        }

        fun XClicked() {
            undoStack.push(Move(row, column, "X", "M"))
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
        saveNow()
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
            undoStackActive.value = false
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
        undoStack = Stack<Move>()
        undoStackActive.value = false
        toastIt("Cleared")
        saveNow()
    }


    fun onUndo() {
        val move = undoStack.pop()
        _board.grid.cells[move.row][move.column].current = move.oldVal
        if (move.newVal == "M")
            _board.marblesPlaced -= 1
        else if (move.oldVal == "M")
            _board.marblesPlaced += 1

        if (undoStack.isEmpty())
            undoStackActive.value = false

        saveNow()
    }

    //implement board.observerForever instead?
    private fun saveNow() {
        repo.updateBoard(_board)
        board.value = _board //setting board value notifies registered observers
    }

    private fun toastIt(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

}