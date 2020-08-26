package io.astefanich.shinro.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.GameSummary
import io.astefanich.shinro.model.plus
import io.astefanich.shinro.repository.ResultsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameSummaryViewModel
@Inject
constructor(
    val summary: GameSummary,
    val resultsRepo: ResultsRepository,
    val calculateScore: @JvmSuppressWildcards (Difficulty, Long) -> Int
) : ViewModel() {

    private var _nextGameDifficulty = MutableLiveData<Difficulty>()
    val nextGameDifficulty: LiveData<Difficulty>
        get() = _nextGameDifficulty

    val pointsEarned = MutableLiveData<Int>()

    init {
        with(summary) {
            _nextGameDifficulty.value = difficulty
            pointsEarned.value =
                if (summary.isWin) calculateScore(difficulty, timeTaken) else 0
        }
        saveToStatistics()
    }


    fun changeDifficulty(newDiff: Difficulty) {
        _nextGameDifficulty.value = newDiff
    }

    fun saveToStatistics(): Unit {
        viewModelScope.launch(Dispatchers.IO) {
            val gameAsAgg = summary.toResultAggregate()
            val targetDiffAgg = resultsRepo.getAggregateForDifficulty(summary.difficulty) + gameAsAgg
            val anyDiffAgg = resultsRepo.getAggregateForDifficulty(Difficulty.ANY) + gameAsAgg
            resultsRepo.updateAggregate(targetDiffAgg)
            resultsRepo.updateAggregate(anyDiffAgg)
            //TODO publish to games leaderboard. calculateAchievements etc
        }
    }

    fun saveToLeaderboard(): Unit {
        with(Dispatchers.IO) {
        }
    }
}