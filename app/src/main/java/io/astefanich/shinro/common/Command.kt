package io.astefanich.shinro.common

data class LoadGameCommand(val playRequest: PlayRequest)
data class MoveCommand(val row: Int, val col: Int)
object ResetBoardCommand
object SetCheckpointCommand
object UndoToCheckpointCommand
object StartGameTimerCommand
object ResumeGameTimerCommand
object PauseGameTimerCommand
object CheckSolutionCommand
object SaveGameCommand
object UndoCommand
object UseFreebieCommand
object SurrenderCommand
object SolveBoardCommand
object TearDownGameCommand
