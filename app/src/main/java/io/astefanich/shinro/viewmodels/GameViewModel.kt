package io.astefanich.shinro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.astefanich.shinro.common.*
import io.astefanich.shinro.model.Game
import io.astefanich.shinro.repository.GameRepository
import io.astefanich.shinro.util.ShinroTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named


/**
 * Core game logic class
 */
class GameViewModel
@Inject
constructor(
    val playRequest: PlayRequest,
    val repo: GameRepository
) : ViewModel() {

    init {
        Timber.i("game viewmodel created")
    }

    @Inject
    @field:Named("gameTimer")
    lateinit var gameTimer: ShinroTimer


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
        class TimeIncremented(val sec: Long): Event()
        object Reset : Event()
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
    private lateinit var checkpoint: Grid
    private var undoStack = Stack<Move>()
    //for xml bindings
    val freebiesRemaining = MutableLiveData<Int>()
    val grid = MutableLiveData<Grid>()
    val checkpointSet = MutableLiveData<Boolean>()
    val undoStackActive = MutableLiveData<Boolean>()
    val difficulty = MutableLiveData<Difficulty>() //not live/mutable but works easier with binding adpater
    //for fragment notifications
    val isLoaded = MutableLiveData<Boolean>()
    val gameEvent = MutableLiveData<Event>()


    init {
        CoroutineScope(Dispatchers.Main).launch { //need to utilize main so lateinit var loads
            _game = when (playRequest) {
                is PlayRequest.Resume -> repo.getActiveGame()
                is PlayRequest.NewGame -> repo.getNewGameByDifficulty(playRequest.difficulty)
            }
            difficulty.value = _game.difficulty
            grid.value = _game.board
            freebiesRemaining.value = if (_game.freebie == Freebie(0, 0)) 1 else 0
            undoStackActive.value = false
            checkpointSet.value = false
            checkpoint = Array(9) { Array(9) { Cell(" ") } }
            delay(3000)
            gameTimer.start {
                _game.timeElapsed += gameTimer.period.seconds
                gameEvent.postValue(Event.TimeIncremented(_game.timeElapsed))
            }
            isLoaded.value = true
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
            is Command.ResumeTimer -> gameTimer.resume()
            is Command.PauseTimer -> gameTimer.pause()
            is Command.SetCheckpoint -> setCheckpoint()
            is Command.UndoToCheckpoint -> undoToCheckpoint()
            is Command.UseFreebie -> useFreebie()
            is Command.SaveGame -> save()
            is Command.Surrender -> completeGame(false)
        }
    }

    private fun setCheckpoint() {
        for (i in 0..8)
            for (j in 0..8)
                checkpoint[i][j] = _game.board[i][j]
        undoStack = Stack<Move>()
        checkpointSet.value = true
        undoStackActive.value = false
    }

    private fun undoToCheckpoint() {
        if (checkpointSet.value!!) {
            for (i in 0..8)
                for (j in 0..8)
                    _game.board[i][j] = checkpoint[i][j]
            undoStack = Stack<Move>()
            undoStackActive.value = false
            checkpointSet.value = false
            grid.postValue(_game.board)
        }
    }


    private fun checkWin() {
        if (_game.marblesPlaced != 12)
            return
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
    private fun move(row: Int, col: Int) {

        val clickedCell = _game.board[row][col]

        //user clicked on arrow or freebie
        if (clickedCell.actual in "A".."G" || Freebie(row, col) == _game.freebie)
            return

        fun OClicked() {
            val newCell = Cell("X", clickedCell.actual)
            undoStack.push(Move(row, col, clickedCell, newCell))
            _game.board[row][col] = newCell
        }

        fun MClicked() {
            val newCell = Cell(" ", clickedCell.actual)
            undoStack.push(Move(row, col, clickedCell, newCell))
            _game.board[row][col] = newCell
            _game.marblesPlaced -= 1 //can win by placing marbles or taking them away
            checkWin()
        }

        fun XClicked() {
            val newCell = Cell("M", clickedCell.actual)
            undoStack.push(Move(row, col, clickedCell, newCell))
            _game.board[row][col] = newCell
            _game.marblesPlaced += 1
            if (_game.marblesPlaced > 12)
                gameEvent.value = Event.TooManyPlaced(_game.marblesPlaced)
            else
                checkWin()
        }

        when (clickedCell.current) {
            " " -> OClicked()
            "M" -> MClicked()
            "X" -> XClicked()
        }
        undoStackActive.value = true
        grid.postValue(_game.board)
    }

    private fun completeGame(isWin: Boolean) {
        val summary = GameSummary(_game.difficulty, isWin, _game.timeElapsed)
        gameTimer.pause()
        gameEvent.value = if (isWin) Event.GameOver.Win(summary) else Event.GameOver.Loss(summary)
    }

    /**
     * Resets board via dialog box
     */
    private fun reset() {
        val cells = _game.board
        for (i in 1..8) {
            for (j in 1..8) {
                if (_game.freebie == Freebie(i, j))
                    continue
                val cell = cells[i][j]
                if (cell.actual == "M" || cell.actual == "X")
                    _game.board[i][j] = Cell(" ", cell.actual)
            }
        }

        with(_game) {
            if (freebie == Freebie(0, 0)) marblesPlaced = 0
            else marblesPlaced = 1
        }
        undoStack = Stack<Move>()
        undoStackActive.value = false
        checkpointSet.value = false
        gameEvent.value = Event.Reset
        grid.postValue(_game.board)
    }


    /**
     * Undoes most recent move
     */
    fun undo() {
        if (undoStack.isEmpty())
            return
        val move = undoStack.pop()
        _game.board[move.row][move.column] = move.oldCell
        if (move.newCell.current == "M")
            _game.marblesPlaced -= 1
        else if (move.oldCell.current == "M")
            _game.marblesPlaced += 1
        grid.postValue(_game.board)
        if (undoStack.isEmpty())
            undoStackActive.value = false
    }

    private fun useFreebie() {
        if (_game.freebie != Freebie(0, 0))
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
                        _game.board[i][j] = Cell("M")
                        checkpoint[i][j] = Cell("M")
                        _game.freebie = Freebie(i, j)
                        gameEvent.value = Event.FreebiePlaced(i, j)
                        break@outer
                    }
                }
            }
            _game.marblesPlaced += 1
            freebiesRemaining.value = (freebiesRemaining.value)?.minus(1)
            grid.postValue(_game.board)
            checkWin()
        }
    }

    private fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveGame(_game)
            Timber.i("game saved. elapsed time: ${_game.timeElapsed}")
        }
    }

    private class Move(val row: Int, val column: Int, val oldCell: Cell, val newCell: Cell)

    fun solve() {
        for (i in 1..8) {
            for (j in 1..8) {
                if(_game.board[i][j].actual == "M"){
                    _game.board[i][j] = Cell("M")
                }
            }
        }
        grid.postValue(_game.board)
        completeGame(true)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("game viewmodel onCleared")
    }
}


