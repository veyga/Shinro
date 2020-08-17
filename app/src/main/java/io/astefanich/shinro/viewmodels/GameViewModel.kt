package io.astefanich.shinro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.astefanich.shinro.domain.*
import io.astefanich.shinro.repository.GameRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

private class Move(val row: Int, val column: Int, val oldVal: String, val newVal: String)

//sealed class GameViewModelCommand {
//    object SaveGame : GameViewModelCommand()
//    data class LoadGame(val req: PlayRequest): GameViewModelCommand()
//}

/**
 * Core game logic class
 */
class GameViewModel
@Inject
constructor(
    playRequest: PlayRequest,
    val repo: GameRepository
) : ViewModel() {

    private lateinit var _game: Game
    val game = MutableLiveData<Game>()

    val undoStackActive = MutableLiveData<Boolean>()
    private var undoStack = Stack<Move>()

    private val _gameWon = MutableLiveData<Boolean>()
    val gameWon: LiveData<Boolean>
        get() = _gameWon

    private val _gameWonBuzz = MutableLiveData<Boolean>()
    val gameWonBuzz: LiveData<Boolean>
        get() = _gameWonBuzz

    val gameLoaded = MutableLiveData<Boolean>()

    init {
        loadGame(playRequest)
        undoStackActive.value = false
    }

    private fun loadGame(req: PlayRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _game = when(req) {
                is PlayRequest.Resume -> repo.getActiveGame()
                is PlayRequest.NewGame -> repo.getNewGameByDifficulty(req.difficulty)
            }
            withContext(Dispatchers.Main){
                game.value = _game
                gameLoaded.value = true
            }
        }
    }

    /**
     * Record a move in this VM
     */
    fun onMove(row: Int, column: Int) {
        val cell = _game.board[row][column]

        //do nothing if board is already complete or user clicks on arrow
        if (gameWon.value == true || cell.actual in "A".."G")
            return

        undoStackActive.value = true

        fun OClicked() {
            undoStack.push(Move(row, column, " ", "X"))
            cell.current = "X"
        }

        fun MClicked() {
            undoStack.push(Move(row, column, "M", " "))
            cell.current = " "
            _game.marblesPlaced -= 1 //can win by placing marbles or taking them away
            if (_game.marblesPlaced == 12)
                checkWin()
        }

        fun XClicked() {
            undoStack.push(Move(row, column, "X", "M"))
            cell.current = "M"
            _game.marblesPlaced += 1
            val mPlaced = _game.marblesPlaced
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
        updateUI()
    }

    private fun checkWin() {
        var numIncorrect = 0
        val cells = _game.board
        for (i in 0..8) {
            for (j in 0..8) {
                val cell = cells[i][j]
                if (cell.current == "M" && cell.actual != "M")
                    numIncorrect += 1
            }
        }
        if (numIncorrect == 0) {
            Timber.i("YOU WON!!!!")
            _gameWon.value = true
            _gameWonBuzz.value = true //notify fragment to buzz
            _gameWonBuzz.value = false //reset it so navigating back doesn't re-trigger the buzz
            undoStackActive.value = false
            saveGame()
        } else
            Timber.i("$numIncorrect of your marbles are in the wrong spots.")
    }

    /**
     * Resets board via dialog box
     */
    fun onReset() {
        val cells = _game.board
        for (i in 0..8) {
            for (j in 0..8) {
                val cell = cells[i][j]
                if (cell.actual == "M" || cell.actual == "X")
                    cell.current = " "
            }
        }
        _game.marblesPlaced = 0
        undoStack = Stack<Move>()
        undoStackActive.value = false
        Timber.i("Cleared")
        updateUI()
    }


    /**
     * Undoes most recent move
     */
    fun onUndo() {
        val move = undoStack.pop()
        _game.board[move.row][move.column].current = move.oldVal
        if (move.newVal == "M")
            _game.marblesPlaced -= 1
        else if (move.oldVal == "M")
            _game.marblesPlaced += 1

        if (undoStack.isEmpty())
            undoStackActive.value = false

        updateUI()
    }


    fun saveGame() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveGame(_game)
        }
        updateUI()
    }

    private fun updateUI() {
        game.value = _game //setting board value notifies registered observers
    }


    fun getSummary(): GameSummary =
        GameSummary(_game.difficulty, _gameWon.value ?: true, _game.timeElapsed)
}

