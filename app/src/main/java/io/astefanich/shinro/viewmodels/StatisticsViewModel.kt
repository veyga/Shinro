package io.astefanich.shinro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.ResultAggregate
import io.astefanich.shinro.database.ResultsDao
import io.astefanich.shinro.model.GameResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StatisticsViewModel
@Inject
constructor(
    val dao: ResultsDao
) : ViewModel() {

    private val _easyStats = MutableLiveData<ResultAggregate>()
    val easyStats: LiveData<ResultAggregate>
        get() = _easyStats

    private val _mediumStats = MutableLiveData<ResultAggregate>()
    val mediumStats: LiveData<ResultAggregate>
        get() = _mediumStats

    private val _hardStats = MutableLiveData<ResultAggregate>()
    val hardStats: LiveData<ResultAggregate>
        get() = _hardStats

    private val _allStats = MutableLiveData<ResultAggregate>()
    val allStats: LiveData<ResultAggregate>
        get() = _allStats

    val show = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val games = dao.getAllResults()
            withContext(Dispatchers.Default) {
                val easyAggregate = calcStats(games) { it.difficulty == Difficulty.EASY }
                val mediumAggregate = calcStats(games) { it.difficulty == Difficulty.MEDIUM }
                val hardAggregate = calcStats(games) { it.difficulty == Difficulty.HARD }
                val allAggregate = calcStats(games) { r: GameResult -> true }
                withContext(Dispatchers.Main) {
                    _easyStats.value = easyAggregate
                    _mediumStats.value = mediumAggregate
                    _hardStats.value = hardAggregate
                    _allStats.value = allAggregate
                    show.value = true
                }
            }
        }
    }


    private suspend fun calcStats(
        results: List<GameResult>,
        predicate: (GameResult) -> Boolean
    ): ResultAggregate {
        val matches = results.filter { predicate(it) }
        if (matches.isEmpty())
            return ResultAggregate(0, 0f, 0, 0)

        var numPlayed = 0
        var numWins = 0
        var totalTime = 0L
        var bestTime = Long.MAX_VALUE
        for (result in matches) {
            numPlayed += 1
            if (result.win)
                numWins += 1
            totalTime += result.time
            if (result.time < bestTime)
                bestTime = result.time
        }
        return ResultAggregate(
            numPlayed = numPlayed,
            winPct = (numWins * 100f) / numPlayed,
            bestTimeSec = bestTime,
            avgTimeSec = totalTime / numPlayed
        )
    }
}