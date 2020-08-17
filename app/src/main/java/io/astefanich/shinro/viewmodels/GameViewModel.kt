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
    val repo: GameRepository,
    val toaster: @JvmSuppressWildcards(true) (String) -> Unit
) : ViewModel() {

//    private var _game: Game
    lateinit var _game: Game
    val game = MutableLiveData<Game>()

    val undoStackActive = MutableLiveData<Boolean>()
    private var undoStack = Stack<Move>()

    private val _gameWon = MutableLiveData<Boolean>()
    val gameWon: LiveData<Boolean>
        get() = _gameWon

    private val _gameWonBuzz = MutableLiveData<Boolean>()
    val gameWonBuzz: LiveData<Boolean>
        get() = _gameWonBuzz


    init {
        Timber.i("game vm created")

//        _game = when (playRequest) {
//            is PlayRequest.Resume -> repo.getActiveGame()
//            is PlayRequest.NewGame -> repo.getNewGameByDifficulty(playRequest.difficulty)
//        }
//        game.value = _game
        loadGame(playRequest)
        undoStackActive.value = false
    }

    private fun loadGame(req: PlayRequest) {
        Timber.i("getting game in vm")
        viewModelScope.launch(Dispatchers.IO) {
            Timber.i("getting game in vm in IO")
            _game = when(req) {
                is PlayRequest.Resume -> repo.getActiveGame()
                is PlayRequest.NewGame -> repo.getNewGameByDifficulty(req.difficulty)
            }
            Timber.i("got the game in vm its ${_game}")
            withContext(Dispatchers.Main){
                Timber.i("setting game in vm")
//                game.value = _game
            }
        }
    }
//        viewModelScope.launch {
//            Timber.i("loading game")
//            delay(3000)
//            _game = when (req) {
//                is PlayRequest.Resume -> repo.getActiveGame()
//                is PlayRequest.NewGame -> repo.getNewGameByDifficulty(req.difficulty)
//            }
//            game.value = _game
//        }


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
                toaster("You have placed ${mPlaced} marbles, which is too many")
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
            toaster("YOU WON!!!!")
            _gameWon.value = true
            _gameWonBuzz.value = true //notify fragment to buzz
            _gameWonBuzz.value = false //reset it so navigating back doesn't re-trigger the buzz
            undoStackActive.value = false
            saveGame()
        } else
            toaster("$numIncorrect of your marbles are in the wrong spots.")
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
        toaster("Cleared")
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
        Timber.i("asking repo to save game")
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

