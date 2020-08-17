package io.astefanich.shinro.repository

import io.astefanich.shinro.database.GameDao
import io.astefanich.shinro.domain.Difficulty
import io.astefanich.shinro.domain.Game
import timber.log.Timber
import javax.inject.Inject

class GameRepository
@Inject
constructor(
    val gameDao: GameDao,
    val boardRepo: BoardRepository
) {

    fun getActiveGame(): Game = gameDao.getActiveGame()

    fun saveGame(game: Game) = gameDao.updateGame(game)

    fun getNewGameByDifficulty(difficulty: Difficulty): Game {
        Timber.i("game repo getting new game")
        val newBoard = boardRepo.getRandomBoardByDifficulty(difficulty)
        val newGame = Game(difficulty = newBoard.difficulty, board = newBoard.cells)
        Timber.i("new game difficulty is ${newGame.difficulty}")
        gameDao.insertGame(newGame)
        return newGame
    }
}