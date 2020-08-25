package io.astefanich.shinro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.Statistic
import io.astefanich.shinro.database.ResultsDao
import io.astefanich.shinro.repository.ResultsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StatisticsViewModel
@Inject
constructor(
    val repo: ResultsRepository
) : ViewModel() {

    private val _easyStats = MutableLiveData<Statistic>()
    val easyStats: LiveData<Statistic>
        get() = _easyStats

    private val _mediumStats = MutableLiveData<Statistic>()
    val mediumStats: LiveData<Statistic>
        get() = _mediumStats

    private val _hardStats = MutableLiveData<Statistic>()
    val hardStats: LiveData<Statistic>
        get() = _hardStats

    private val _allStats = MutableLiveData<Statistic>()
    val allStats: LiveData<Statistic>
        get() = _allStats

    val show = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            _easyStats.value = repo.getStatisticForDifficulty(Difficulty.EASY)
            _mediumStats.value = repo.getStatisticForDifficulty(Difficulty.MEDIUM)
            _hardStats.value = repo.getStatisticForDifficulty(Difficulty.HARD)
            _allStats.value = repo.getStatisticForDifficulty(Difficulty.ANY)
            withContext(Dispatchers.Main){
                show.value = true
            }
        }
//        viewModelScope.launch(Dispatchers.IO) {
//            val games = dao.getAllResults()
//            withContext(Dispatchers.Default) {
//                val easyAggregate = calcStats(games) { it.difficulty == Difficulty.EASY }
//                val mediumAggregate = calcStats(games) { it.difficulty == Difficulty.MEDIUM }
//                val hardAggregate = calcStats(games) { it.difficulty == Difficulty.HARD }
//                val allAggregate = calcStats(games) { r: GameResult -> true }
//                withContext(Dispatchers.Main) {
//                    _easyStats.value = easyAggregate
//                    _mediumStats.value = mediumAggregate
//                    _hardStats.value = hardAggregate
//                    _allStats.value = allAggregate
//                    show.value = true
//                }
//            }
//        }
    }


//    private suspend fun calcStats(
//        results: List<GameResult>,
//        predicate: (GameResult) -> Boolean
//    ): Statistic {
//        val matches = results.filter { predicate(it) }
//        if (matches.isEmpty())
//            return Statistic(0, 0f, 0, 0)
//
//        var numPlayed = 0
//        var numWins = 0
//        var totalTime = 0L
//        var bestTime = Long.MAX_VALUE
//        for (result in matches) {
//            numPlayed += 1
//            if (result.win)
//                numWins += 1
//            totalTime += result.time
//            if (result.time < bestTime)
//                bestTime = result.time
//        }
//        return Statistic(
//            numPlayed = numPlayed,
//            winPct = (numWins * 100f) / numPlayed,
//            bestTimeSec = bestTime,
//            avgTimeSec = totalTime / numPlayed
//        )
//    }
}