package io.astefanich.shinro.repository

import io.astefanich.shinro.database.GameDao
import io.astefanich.shinro.domain.Difficulty
import io.astefanich.shinro.domain.Game
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class GameRepository
@Inject
constructor(
    val gameDao: GameDao,
    val boardRepo: BoardRepository,
    val firstGame: Game
) {

    suspend fun getActiveGame(): Game {
        //race condition in testing. DB callback needs to insert board first
        //remove this check when loading DB directly from file.
        val activeGame = gameDao.getActiveGame()
        if(activeGame == null){
            gameDao.insertGame(firstGame)
            return firstGame
        }
        return activeGame
    }

    suspend fun saveGame(game: Game) = gameDao.updateGame(game)

    suspend fun getNewGameByDifficulty(difficulty: Difficulty): Game {
        Timber.i("game repo getting new game")
        delay(3000)
        val newBoard = boardRepo.getRandomBoardByDifficulty(difficulty)
        val newGame = Game(difficulty = newBoard.difficulty, board = newBoard.cells)
        Timber.i("new game difficulty is ${newGame.difficulty}")
        gameDao.insertGame(newGame)
        return newGame
    }
}