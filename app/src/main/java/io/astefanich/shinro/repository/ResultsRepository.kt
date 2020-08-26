package io.astefanich.shinro.repository

import io.astefanich.shinro.common.Difficulty
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

    suspend fun getAggregateForDifficulty(difficulty: Difficulty): ResultAggregate {
        return withContext(Dispatchers.IO){
            dao.getAggregateByDifficulty(difficulty)
        }
    }

    suspend fun updateAggregate(aggregate: ResultAggregate){
        withContext(Dispatchers.IO){
            dao.updateAggregate(aggregate)
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


}