package io.astefanich.shinro.common

data class GameLoadedEvent(val difficulty: Difficulty, val grid: Grid, val startTime: Long, val freebiesRemaining: Int)
data class MoveRecordedEvent(val row: Int, val col: Int, val newVal: String)
data class CellUndoneEvent(val row: Int, val col: Int, val newVal: String)
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
object GameWonEvent
object GameLostEvent
data class GameTornDownEvent(val summary: GameSummary)

