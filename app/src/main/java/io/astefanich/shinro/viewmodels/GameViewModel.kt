package io.astefanich.shinro.viewmodels

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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import java.util.*
import javax.inject.Inject


data class LoadGameCommand(val playRequest: PlayRequest)
data class MoveCommand(val row: Int, val col: Int)
object ResetBoardCommand
object SetCheckpointCommand
object UndoToCheckpointCommand
object StartTimerCommand
object ResumeTimerCommand
object PauseTimerCommand
object CheckSolutionCommand
object SaveGameCommand
object UndoCommand
object UseFreebieCommand
object SurrenderCommand
object SolveBoardCommand


data class GameLoadedEvent(val difficulty: Difficulty, val grid: Grid, val elapsedTime: Long, val freebiesRemaining: Int )
data class TimeIncrementedEvent(val sec: Long)
data class CellChangedEvent(val row: Int, val col: Int, val newVal: String)
object TwelveMarblesPlacedEvent
data class RevertedToCheckpointEvent(val newBoard: Grid)
data class BoardResetEvent(val newBoard: Grid)
object UndoStackActivatedEvent
object UndoStackDeactivatedEvent
object CheckpointSetEvent
object CheckpointResetEvent
object CheckpointDeactivatedEvent
data class FreebiePlacedEvent(val row: Int, val col: Int, val nRemaining: Int)
object OutOfFreebiesEvent
data class IncorrectSolutionEvent(val numIncorrect: Int)
data class TooManyPlacedEvent(val numPlaced: Int)
data class GameCompletedEvent(val summary: GameSummary)
data class BoardSolvedEvent(val newBoard: Grid)

/**
 * Core game logic class
 */
class GameViewModel
@Inject
constructor(
    val repo: GameRepository,
    val gameTimer: ShinroTimer
//    val bus: EventBus
) : ViewModel() {

    init {
        Timber.i("game viewmodel created")
//        bus.register(this)
    }

    private var bus = EventBus.getDefault()

    private lateinit var _game: Game
    private var checkpoint: Grid = Array(9) { Array(9) { Cell(" ") } }
    private var checkpointActive = false
    private var undoStack = Stack<Move>()

    @Subscribe
    fun handle(cmd: LoadGameCommand) {
        CoroutineScope(Dispatchers.Main).launch { //need to utilize main so lateinit var loads
            _game = when (cmd.playRequest) {
                is PlayRequest.Resume -> repo.getActiveGame()
                is PlayRequest.NewGame -> repo.getNewGameByDifficulty(cmd.playRequest.difficulty)
            }
            val freebiesRemaining = if (_game.freebie == Freebie(0, 0)) 1 else 0
            delay(2000)
            bus.post(
                GameLoadedEvent(
                    _game.difficulty,
                    _game.board,
                    _game.timeElapsed,
                    freebiesRemaining
                )
            )
        }
    }

    @Subscribe
    fun handle(cmd: StartTimerCommand) {
        gameTimer.start {
            _game.timeElapsed += gameTimer.period.seconds
            bus.post(TimeIncrementedEvent(_game.timeElapsed))
        }
    }

    @Subscribe
    fun handle(cmd: MoveCommand) {
        val r = cmd.row
        val c = cmd.col
        val clicked = _game.board[r][c]
        //user clicked on arrow or freebie
        if (clicked.actual in "A".."G" || Freebie(r, c) == _game.freebie)
            return

        var newCell: Cell = Cell("", "")
        if (clicked.current == " ") {
            newCell = Cell("X", clicked.actual)
        } else if (clicked.current == "M") {
            newCell = Cell(" ", clicked.actual)
            _game.marblesPlaced -= 1 //can win by placing marbles or taking them away
        } else {
            newCell = Cell("M", clicked.actual)
            _game.marblesPlaced += 1
        }
        _game.board[r][c] = newCell
        bus.post(CellChangedEvent(r, c, newCell.current))
        undoStack.push(Move(r, c, clicked, newCell))
        bus.post(UndoStackActivatedEvent)
        if (_game.marblesPlaced > 12)
            bus.post(TooManyPlacedEvent(_game.marblesPlaced))
        else if (_game.marblesPlaced == 12)
            bus.post(TwelveMarblesPlacedEvent)
    }

    @Subscribe
    fun handle(cmd: CheckSolutionCommand) {
        if (_game.marblesPlaced > 12) {
            bus.post(TooManyPlacedEvent(_game.marblesPlaced))
        } else if (_game.marblesPlaced == 12) {
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
                bus.post(GameCompletedEvent(GameSummary(_game.difficulty, true, _game.timeElapsed)))
            else
                bus.post(IncorrectSolutionEvent(numIncorrect))
        }
    }


    @Subscribe
    fun handle(cmd: ResetBoardCommand) {
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
        checkpointActive = false
        bus.post(UndoStackDeactivatedEvent)
        bus.post(CheckpointDeactivatedEvent)
        bus.post(BoardResetEvent(_game.board))
    }

    @Subscribe
    fun handle(cmd: SetCheckpointCommand) {
        for (i in 0..8)
            for (j in 0..8)
                checkpoint[i][j] = _game.board[i][j]
        if (checkpointActive)
            bus.post(CheckpointResetEvent)
        else {
            bus.post(CheckpointSetEvent)
            checkpointActive = true
        }
        undoStack = Stack<Move>()
        bus.post(UndoStackDeactivatedEvent)
    }

    @Subscribe
    fun handle(cmd: UndoToCheckpointCommand) {
        for (i in 0..8)
            for (j in 0..8)
                _game.board[i][j] = checkpoint[i][j]
        undoStack = Stack<Move>()
        checkpointActive = false
        bus.post(UndoStackDeactivatedEvent)
        bus.post(CheckpointDeactivatedEvent)
        bus.post(RevertedToCheckpointEvent(_game.board))
    }

    @Subscribe
    fun handle(cmd: ResumeTimerCommand) {
        gameTimer.resume()
    }

    @Subscribe
    fun handle(cmd: PauseTimerCommand) {
        gameTimer.pause()
    }

    @Subscribe
    fun handle(cmd: SaveGameCommand) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveGame(_game)
            Timber.i("game saved. elapsed time: ${_game.timeElapsed}")
        }
    }

    @Subscribe
    fun handle(cmd: UndoCommand) {
        if (undoStack.isEmpty())
            return
        val move = undoStack.pop()
        _game.board[move.row][move.col] = move.oldCell
        if (move.newCell.current == "M")
            _game.marblesPlaced -= 1
        else if (move.oldCell.current == "M")
            _game.marblesPlaced += 1
        if (undoStack.isEmpty())
            bus.post(UndoStackDeactivatedEvent)
        bus.post(CellChangedEvent(move.row, move.col, move.oldCell.current))

    }

    @Subscribe
    fun handle(cmd: UseFreebieCommand) {
        if (_game.freebie != Freebie(0, 0)) {
            bus.post(OutOfFreebiesEvent)
            return
        }
        val ranges = arrayOf((1..8), (8 downTo 1))
        val rand = Random()
        val verticalStrategy = ranges[rand.nextInt(2)]
        val horizontalStrategy = ranges[rand.nextInt(2)]
        outer@ for (i in verticalStrategy) {
            for (j in horizontalStrategy) {
                val cell = _game.board[i][j]
                if (cell.actual == "M" && cell.current != "M") {
                    _game.board[i][j] = Cell("M")
//                    bus.post(CellChangedEvent(i, j, "M"))
                    checkpoint[i][j] = Cell("M")
                    _game.freebie = Freebie(i, j)
                    bus.post(FreebiePlacedEvent(i, j, 0))
                    break@outer
                }
            }
        }
        _game.marblesPlaced += 1
        if (_game.marblesPlaced == 12)
            bus.post(TwelveMarblesPlacedEvent)
    }

    @Subscribe
    fun handle(cmd: SurrenderCommand) {
        bus.post(GameCompletedEvent(GameSummary(_game.difficulty, false, _game.timeElapsed)))
    }

    @Subscribe
    fun solve(cmd: SolveBoardCommand) {
        for (i in 1..8) {
            for (j in 1..8) {
                if (_game.board[i][j].actual == "M") {
                    _game.board[i][j] = Cell("M")
                }
            }
        }
        bus.post(BoardSolvedEvent(_game.board))
    }

    private class Move(val row: Int, val col: Int, val oldCell: Cell, val newCell: Cell)
}


