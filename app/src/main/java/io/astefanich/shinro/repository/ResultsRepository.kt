package io.astefanich.shinro.repository

import androidx.lifecycle.LiveData
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.GameSummary
import io.astefanich.shinro.common.Statistic
import io.astefanich.shinro.database.ResultsDao
import io.astefanich.shinro.model.ResultAggregate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ResultsRepository
@Inject
constructor(
    val dao: ResultsDao
) {

    //calculating total statistics upon entry into DB
    suspend fun addGameToAggregate(game: GameSummary) {
        withContext(Dispatchers.IO) {
            val targetDifficulty = dao.getAggregateByDifficulty(game.difficulty)
            val anyDifficulty = dao.getAggregateByDifficulty(Difficulty.ANY)
            Timber.i("targetAggregate is ${targetDifficulty}")
            val updated = targetDifficulty + game.toResultAggregate()
            Timber.i("targetUpdated is $updated")
            dao.updateAggregate(targetDifficulty + game.toResultAggregate())
            dao.updateAggregate(anyDifficulty + game.toResultAggregate())
        }
    }

    suspend fun getStatisticForDifficulty(difficulty: Difficulty): Statistic {
        dao.getAggregateByDifficulty(Difficulty.ANY)
        delay(500) //TODO race condition. aggregates need to load first
        Timber.i("statistics for ${difficulty.repr} is ${dao.getAggregateByDifficulty(difficulty)}")
        return with(dao.getAggregateByDifficulty(difficulty)) {
            Statistic(
                difficulty = difficulty,
                numPlayed = numPlayed,
                winPct = (numWins * 100f) / numPlayed,
                bestTimeSec = if (bestTimeSec == Long.MAX_VALUE) 0 else bestTimeSec,
                avgTimeSec = if (numPlayed == 0) 0 else totalTimeSeconds / numPlayed
            )
        }
    }

    fun GameSummary.toResultAggregate(): ResultAggregate =
        ResultAggregate(
            difficulty = this.difficulty,
            numPlayed = 1,
            numWins = if (isWin) 1 else 0,
            bestTimeSec = time,
            totalTimeSeconds = time
        )

    infix operator fun ResultAggregate.plus(other: ResultAggregate): ResultAggregate =
        ResultAggregate(
            id = this.id,
            difficulty = this.difficulty,
            numPlayed = this.numPlayed + other.numPlayed,
            numWins = this.numWins + other.numWins,
            bestTimeSec = minOf(this.bestTimeSec, other.bestTimeSec),
            totalTimeSeconds = this.totalTimeSeconds + other.totalTimeSeconds
        )
}