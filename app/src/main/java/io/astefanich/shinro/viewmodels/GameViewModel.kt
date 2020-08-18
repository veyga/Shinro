package io.astefanich.shinro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.astefanich.shinro.domain.Game
import io.astefanich.shinro.domain.GameSummary
import io.astefanich.shinro.domain.PlayRequest
import io.astefanich.shinro.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


/**
 * Core game logic class
 */
class GameViewModel
@Inject
constructor(
    val playRequest: PlayRequest,
    val repo: GameRepository
) : ViewModel() {

    sealed class Command {
        data class Move(val row: Int, val col: Int) : Command()
        object Reset : Command()
        object SetCheckpoint : Command()
        object UndoToCheckpoint : Command()
        object SaveGame : Command()
        object Undo : Command()
        object UseFreebie : Command()
        object Surrender : Command()
    }

    sealed class Event {
        object Loaded : Event()
        object Reset : Event()
        object CheckpointSet: Event()
        object RevertedToCheckpoint: Event()
        class IncorrectSolution(val numIncorrect: Int) : Event()
        class TooManyPlaced(val numPlaced: Int) : Event()
        sealed class GameOver(val summary: GameSummary) : Event() {
            class Win(summary: GameSummary) : GameOver(summary)
            class Loss(summary: GameSummary) : GameOver(summary)
        }
    }

    private lateinit var _game: Game
    val game = MutableLiveData<Game>()

    private val _gameEvent = MutableLiveData<Event>()
    val gameEvent: LiveData<Event>
        get() = _gameEvent

    private var undoStack = Stack<Move>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _game = when (playRequest) {
                is PlayRequest.Resume -> repo.getActiveGame()
                is PlayRequest.NewGame -> repo.getNewGameByDifficulty(playRequest.difficulty)
            }
            withContext(Dispatchers.Main) {
                game.value = _game
                _gameEvent.value = Event.Loaded
            }
        }
    }

    /**
     * Accept a command on the game state
     */
    fun accept(cmd: Command) {
        when (_gameEvent.value) {
            is Event.GameOver -> return
        }
        when (cmd) {
            is Command.Move -> move(cmd.row, cmd.col)
            is Command.Reset -> reset()
            is Command.Undo -> undo()
            is Command.SaveGame -> save()
            is Command.Surrender -> completeGame(false)
            else -> {}
        }
    }

    /**
     * Record a move in this VM
     */
    private fun move(row: Int, column: Int) {
        fun checkWin() {
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
                completeGame(true)
            } else
                _gameEvent.value = Event.IncorrectSolution(numIncorrect)

        }

        val cell = _game.board[row][column]

        //user clicked on arrow
        if (cell.actual in "A".."G")
            return

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
                _gameEvent.value = Event.TooManyPlaced(mPlaced)
        }

        when (cell.current) {
            " " -> OClicked()
            "M" -> MClicked()
            "X" -> XClicked()
        }

        updateUI()
    }

    private fun completeGame(win: Boolean) {
        val summary = GameSummary(_game.difficulty, win, _game.timeElapsed)
        _gameEvent.value = if (win) Event.GameOver.Win(summary) else Event.GameOver.Loss(summary)
        save()
    }

    /**
     * Resets board via dialog box
     */
    private fun reset() {
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
        _gameEvent.value = Event.Reset
        updateUI()
    }


    /**
     * Undoes most recent move
     */
    fun undo() {
        if (undoStack.isNotEmpty()) {
            val move = undoStack.pop()
            _game.board[move.row][move.column].current = move.oldVal
            if (move.newVal == "M")
                _game.marblesPlaced -= 1
            else if (move.oldVal == "M")
                _game.marblesPlaced += 1
            updateUI()
        }
    }


    private fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveGame(_game)
        }
        updateUI()
    }

    private fun updateUI() {
        game.value = _game //setting board value notifies registered observers
    }

}

private class Move(val row: Int, val column: Int, val oldVal: String, val newVal: String)
