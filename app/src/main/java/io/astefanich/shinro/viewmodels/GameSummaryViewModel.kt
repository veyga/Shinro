package io.astefanich.shinro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.GameSummary
import io.astefanich.shinro.repository.ResultsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameSummaryViewModel
@Inject
constructor(
    val summary: GameSummary,
    val resultsRepo: ResultsRepository
) : ViewModel() {

    private var _nextGameDifficulty = MutableLiveData<Difficulty>()
    val nextGameDifficulty: LiveData<Difficulty>
        get() = _nextGameDifficulty

    val pointsEarned = MutableLiveData<Int>()

    init {
        with(summary) {
            _nextGameDifficulty.value = difficulty
            pointsEarned.value =
                if (summary.isWin) calcScore(difficulty = difficulty, timeTaken = time) else 0
        }
        saveToStatistics()
    }

    //taking 0 seconds doubles your score. You never drop below base on a win
    fun calcScore(difficulty: Difficulty, timeTaken: Long): Int {
        data class ScorePair(val baseScore: Int, val allottedTime: Int)
        val scoreMap: Map<Difficulty, ScorePair> =
            mapOf(
                Difficulty.EASY to ScorePair(2000, 5 * 60), //easy = 5min
                Difficulty.MEDIUM to ScorePair(5000, 10 * 60), //medium = 10min
                Difficulty.HARD to ScorePair(10000, 20 * 60) //hard = 20min
            )
        val pair = scoreMap.get(difficulty)!!
        val timeBonus = pair.allottedTime - timeTaken
        return (pair.baseScore + ((pair.baseScore / pair.allottedTime) * timeBonus)).toInt()
    }

    fun changeDifficulty(newDiff: Difficulty) {
        _nextGameDifficulty.value = newDiff
    }

    fun saveToStatistics(): Unit {
        viewModelScope.launch(Dispatchers.IO) {
            resultsRepo.addGameToAggregate(summary)
        }
    }

    fun saveToLeaderboard(): Unit {
        with(Dispatchers.IO) {
        }
    }
}