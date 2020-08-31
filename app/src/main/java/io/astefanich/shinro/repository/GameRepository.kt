package io.astefanich.shinro.repository

import android.content.SharedPreferences
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.database.GameDao
import io.astefanich.shinro.model.Game
import timber.log.Timber
import javax.inject.Inject

class GameRepository
@Inject
constructor(
    val gameDao: GameDao,
    val boardRepo: BoardRepository,
    val prefs: SharedPreferences,
) {

    suspend fun getActiveGame(): Game = gameDao.getActiveGame()
//        gameDao.getActiveGame() ?: getNewGameByDifficulty(Difficulty.EASY)
    //should only be null on very first game


    suspend fun saveGame(game: Game) = gameDao.updateGame(game)

    suspend fun getNewGameByDifficulty(difficulty: Difficulty): Game {
        val newBoard = boardRepo.getRandomBoardByDifficulty(difficulty)
        val newGame = Game(difficulty = newBoard.difficulty, board = newBoard.cells)
        gameDao.insertGame(newGame)
        Timber.i("now has active game")
        prefs.edit().putBoolean("has_active_game", true).apply()
        return newGame
    }
}