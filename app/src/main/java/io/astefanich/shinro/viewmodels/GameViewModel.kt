package io.astefanich.shinro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.astefanich.shinro.model.Game
import io.astefanich.shinro.common.GameSummary
import io.astefanich.shinro.common.PlayRequest
import io.astefanich.shinro.common.TimePeriod
import io.astefanich.shinro.repository.GameRepository
import io.astefanich.shinro.util.GameTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


/**
 * Core game logic class
 */
class GameViewModel
@Inject
constructor(
    val playRequest: PlayRequest,
    val repo: GameRepository,
    val timer: GameTimer
) : ViewModel() {

    //Commands to apply to model
    //Corresponding events are for user notifications/navigation
    sealed class Command {
        data class Move(val row: Int, val col: Int) : Command()
        object Reset : Command()
        object SetCheckpoint : Command()
        object UndoToCheckpoint : Command()
        object ResumeTimer : Command()
        object PauseTimer : Command()
        object SaveGame : Command()
        object Undo : Command()
        object UseFreebie : Command()
        object Surrender : Command()
    }

    sealed class Event {
        object Loaded : Event()
        object Reset : Event()
        object CheckpointSet : Event()
        object RevertedToCheckpoint : Event()
        class FreebiePlaced(val row: Int, val col: Int) : Event()
        object OutOfFreebies : Event()
        class IncorrectSolution(val numIncorrect: Int) : Event()
        class TooManyPlaced(val numPlaced: Int) : Event()
        sealed class GameOver(val summary: GameSummary) : Event() {
            class Win(summary: GameSummary) : GameOver(summary)
            class Loss(summary: GameSummary) : GameOver(summary)
        }
    }

    private lateinit var _game: Game
    val game = MutableLiveData<Game>()
    val gameEvent = MutableLiveData<Event>()
    private var undoStack = Stack<Move>()


    init {
        CoroutineScope(Dispatchers.Main).launch { //need to utilize main so lateinit var loads
            _game = when (playRequest) {
                is PlayRequest.Resume -> repo.getActiveGame()
                is PlayRequest.NewGame -> repo.getNewGameByDifficulty(playRequest.difficulty)
            }
            game.value = _game
            gameEvent.value = Event.Loaded
            delay(2000)
            timer.start {
                Timber.i("accccction")
                _game.timeElapsed += timer.period.seconds
                updateUI()
            }
        }
    }


    /**
     * Accept a command on the game state
     */
    fun accept(cmd: Command) {
        when (gameEvent.value) {
            is Event.GameOver -> return
        }
        when (cmd) {
            is Command.Move -> move(cmd.row, cmd.col)
            is Command.Reset -> reset()
            is Command.Undo -> undo()
            is Command.ResumeTimer -> timer.resume()
            is Command.PauseTimer -> timer.pause()
            is Command.UseFreebie -> useFreebie()
            is Command.SaveGame -> save()
            is Command.Surrender -> completeGame(false)
            else -> {
            }
        }
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
        if (numIncorrect == 0)
            completeGame(true)
        else
            gameEvent.value = Event.IncorrectSolution(numIncorrect)
    }

    /**
     * Record a move in this VM
     */
    private fun move(row: Int, column: Int) {

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
                gameEvent.value = Event.TooManyPlaced(mPlaced)
        }

        when (cell.current) {
            " " -> OClicked()
            "M" -> MClicked()
            "X" -> XClicked()
        }

        updateUI()
    }

    private fun completeGame(isWin: Boolean) {
        val summary = GameSummary(_game.difficulty, isWin, game.value!!.timeElapsed)
        timer.pause()
        gameEvent.value = if (isWin) Event.GameOver.Win(summary) else Event.GameOver.Loss(summary)
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
        gameEvent.value = Event.Reset
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

    private fun useFreebie() {
        if (_game.freebiesRemaining == 0)
            gameEvent.value = Event.OutOfFreebies
        else {
            val ranges = arrayOf((1..8), (8 downTo 1))
            val rand = Random()
            val verticalStrategy = ranges[rand.nextInt(2)]
            val horizontalStrategy = ranges[rand.nextInt(2)]
            outer@ for (i in verticalStrategy) {
                for (j in horizontalStrategy) {
                    val cell = _game.board[i][j]
                    if (cell.actual == "M" && cell.current != "M") {
                        cell.current = "M"
                        gameEvent.value = Event.FreebiePlaced(i, j)
                        break@outer
                    }
                }
            }
            _game.freebiesRemaining -= 1
            _game.marblesPlaced += 1
            updateUI()
            if (_game.marblesPlaced == 12)
                checkWin()
        }
    }

    private fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveGame(_game)
            Timber.i("game saved")
        }
    }

    private fun updateUI() {
        game.postValue(_game)  //triggers databinding
    }

    private class Move(val row: Int, val column: Int, val oldVal: String, val newVal: String)
}


