package io.astefanich.shinro.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.astefanich.shinro.common.*
import io.astefanich.shinro.model.Game
import io.astefanich.shinro.repository.GameRepository
import io.astefanich.shinro.util.ShinroTimer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import java.util.*
import javax.inject.Inject


/**
 * Core game logic class
 */
class GameViewModel
@Inject
constructor(
    val repo: GameRepository,
    val bus: EventBus,
    val gameTimer: ShinroTimer,
    val checkpoint: Grid
) : ViewModel() {

    init {
        if (!bus.isRegistered(this))
            bus.register(this)
    }

    private lateinit var _game: Game
    private var checkpointActive = false
    private var undoStack = Stack<Move>()

    @Subscribe
    fun handle(cmd: LoadGameCommand) {
        if (gameTimer.isStarted) {
            _game.apply {
                bus.post(GameLoadedEvent(difficulty, board, timeElapsed, freebiesRemaining()))
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                _game = when (cmd.playRequest) {
                    is PlayRequest.Resume -> repo.getActiveGame()
                    is PlayRequest.NewGame -> repo.getNewGameByDifficulty(cmd.playRequest.difficulty)
                }
                //back stack navigation can lead to completed boards being reloaded
                //game --> summary --> home --> game  (home always asks to resume game)
                if (_game.isComplete)
                    _game = repo.getNewGameByDifficulty(_game.difficulty)
                withContext(Dispatchers.Main) {
                    _game.apply {
                        bus.post(
                            GameLoadedEvent(
                                difficulty,
                                board,
                                timeElapsed,
                                freebiesRemaining()
                            )
                        )
                    }
                }
            }
        }
    }

    @Subscribe
    fun handle(cmd: StartGameTimerCommand) {
        gameTimer.run {
            if (!isStarted)
                start { _game.timeElapsed += 1L }
        }
    }

    @Subscribe
    fun handle(cmd: ResumeGameTimerCommand) {
        gameTimer.resume()
    }

    @Subscribe
    fun handle(cmd: PauseGameTimerCommand) {
        gameTimer.pause()
    }

    @Subscribe
    fun handle(cmd: MoveCommand) {
        val r = cmd.row
        val c = cmd.col
        _game.apply {
            val clicked = board[r][c]
            if (clicked.actual in "A".."G" || Freebie(r, c) == freebie)
                return
            if (undoStack.isEmpty())
                bus.post(UndoStackActivatedEvent)
            var newCell: Cell
            when (clicked.current) {
                " " -> newCell = Cell("X", clicked.actual)
                "M" -> {
                    newCell = Cell(" ", clicked.actual)
                    marblesPlaced -= 1 //can win by placing marbles or taking them away
                }
                else -> {
                    newCell = Cell("M", clicked.actual)
                    marblesPlaced += 1
                    if (marblesPlaced > 12)
                        bus.post(TooManyPlacedEvent(marblesPlaced))
                }
            }
            board[r][c] = newCell
            bus.post(MoveRecordedEvent(r, c, newCell.current))
            undoStack.push(Move(r, c, clicked, newCell))
            if (marblesPlaced == 12)
                bus.post(TwelveMarblesPlacedEvent)
        }
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
            if (numIncorrect == 0) {
                _game.isWin = true
                _game.isComplete = true
                bus.post(GameWonEvent)
            } else
                bus.post(IncorrectSolutionEvent(numIncorrect))
        }
    }


    @Subscribe
    fun handle(cmd: ResetBoardCommand) {
        with(_game) {
            val cells = board
            for (i in 1..8) {
                for (j in 1..8) {
                    if (freebie == Freebie(i, j))
                        continue
                    val cell = cells[i][j]
                    if (cell.actual == "M" || cell.actual == "X")
                        board[i][j] = Cell(" ", cell.actual)
                }
            }
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
        if (checkpointActive) {
            _game.marblesPlaced = 0
            for (i in 0..8) {
                for (j in 0..8) {
                    _game.board[i][j] = checkpoint[i][j]
                    if (_game.board[i][j].current == "M")
                        _game.marblesPlaced += 1
                }
            }
            undoStack = Stack<Move>()
            checkpointActive = false
            bus.post(UndoStackDeactivatedEvent)
            bus.post(CheckpointDeactivatedEvent)
            bus.post(RevertedToCheckpointEvent(_game.board))
        }
    }


    @Subscribe
    fun handle(cmd: SaveGameCommand) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveGame(_game)
        }
    }

    @Subscribe
    fun handle(cmd: TearDownGameCommand) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveGame(_game)
            withContext(Dispatchers.Main) {
                bus.post(
                    GameTornDownEvent(
                        GameSummary(
                            _game.difficulty,
                            _game.isWin,
                            _game.timeElapsed
                        )
                    )
                )
                bus.unregister(this) //stop accepting commands
            }
        }
    }

    @Subscribe
    fun handle(cmd: UndoCommand) {
        if (undoStack.isEmpty())
            return
        val move = undoStack.pop()
        with(_game) {
            board[move.row][move.col] = move.oldCell
            if (move.newCell.current == "M") marblesPlaced -= 1
            else if (move.oldCell.current == "M") marblesPlaced += 1
        }
        if (undoStack.isEmpty())
            bus.post(UndoStackDeactivatedEvent)
        bus.post(CellUndoneEvent(move.row, move.col, move.oldCell.current))

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
        with(_game) {
            outer@ for (i in verticalStrategy) {
                for (j in horizontalStrategy) {
                    val cell = _game.board[i][j]
                    if (cell.actual == "M" && cell.current != "M") {
                        board[i][j] = Cell("M")
                        checkpoint[i][j] = Cell("M")
                        freebie = Freebie(i, j)
                        bus.post(FreebiePlacedEvent(i, j, 0))
                        break@outer
                    }
                }
            }
            marblesPlaced += 1
            if (marblesPlaced == 12)
                bus.post(TwelveMarblesPlacedEvent)
        }
    }

    @Subscribe
    fun handle(cmd: SurrenderCommand) {
        _game.isComplete = true
        _game.isWin = false
        bus.post(GameLostEvent)
    }

    @Subscribe
    fun handle(cmd: SolveBoardCommand) {
        for (i in 1..8) {
            for (j in 1..8) {
                if (_game.board[i][j].actual == "M") {
                    _game.board[i][j] = Cell("M")
                }
            }
        }
        bus.post(RevertedToCheckpointEvent(_game.board)) //trigger grid refresh
        _game.isComplete = true
        _game.isWin = true
        bus.post(GameWonEvent)
    }

    override fun onCleared() {
        gameTimer.pause()
        if (bus.isRegistered(this))
            bus.unregister(this)
        super.onCleared()
    }

    private class Move(val row: Int, val col: Int, val oldCell: Cell, val newCell: Cell)
}


