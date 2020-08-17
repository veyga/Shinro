package io.astefanich.shinro.repository

import io.astefanich.shinro.database.GameDao
import io.astefanich.shinro.domain.Difficulty
import io.astefanich.shinro.domain.Game
import javax.inject.Inject

class GameRepository
@Inject
constructor(
    val gameDao: GameDao,
    val boardRepo: BoardRepository
) {

    suspend fun getActiveGame(): Game =
        gameDao.getActiveGame() ?: getNewGameByDifficulty(Difficulty.EASY)
    //should only be null on very first game


    suspend fun saveGame(game: Game) = gameDao.updateGame(game)

    suspend fun getNewGameByDifficulty(difficulty: Difficulty): Game {
        val newBoard = boardRepo.getRandomBoardByDifficulty(difficulty)
        val newGame = Game(difficulty = newBoard.difficulty, board = newBoard.cells)
        gameDao.insertGame(newGame)
        return newGame
    }
}