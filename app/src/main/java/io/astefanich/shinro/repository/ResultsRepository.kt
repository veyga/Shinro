package io.astefanich.shinro.repository

import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.GameSummary
import io.astefanich.shinro.common.Statistic
import io.astefanich.shinro.database.ResultsDao
import io.astefanich.shinro.model.ResultAggregate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            val resultAggregate = game.toResultAggregate()
            dao.updateAggregate(targetDifficulty + resultAggregate)
            dao.updateAggregate(anyDifficulty + resultAggregate)
        }
    }

    suspend fun getStatisticForDifficulty(difficulty: Difficulty): Statistic {
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

    infix operator fun ResultAggregate.plus(o: ResultAggregate): ResultAggregate =
        ResultAggregate(
            id = this.id,
            difficulty = this.difficulty,
            numPlayed = this.numPlayed + o.numPlayed,
            numWins = this.numWins + o.numWins,
            bestTimeSec = if (o.numWins == 1) minOf(bestTimeSec, o.bestTimeSec) else bestTimeSec,
            totalTimeSeconds = this.totalTimeSeconds + o.totalTimeSeconds
        )
}