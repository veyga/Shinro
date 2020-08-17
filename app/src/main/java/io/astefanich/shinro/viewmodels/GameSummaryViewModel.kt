package io.astefanich.shinro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Difficulty
import javax.inject.Inject

class GameSummaryViewModel
@Inject
constructor(
    val previousDifficulty: Difficulty,
    val win: Boolean,
    val time: Long
) : ViewModel() {

    private var _nextGameDifficulty = MutableLiveData<Difficulty>()
    val nextGameDifficulty: LiveData<Difficulty>
        get() = _nextGameDifficulty

    var pointsEarned: Int

    var animTime: Long

    init {
        _nextGameDifficulty.value = previousDifficulty
        val (time, base) = if (!win) Pair(0,0) else when (previousDifficulty) {
            Difficulty.EASY -> Pair(1000, 25000)
            Difficulty.MEDIUM -> Pair(2500, 50000)
            Difficulty.HARD -> Pair(4000, 100000)
        }
        animTime = time.toLong()
        pointsEarned = base
    }

    fun difficultyChanged(newDiff: Difficulty) {
        _nextGameDifficulty.value = newDiff
    }

    fun saveToStatistics(): Unit = TODO()
    fun saveToLeaderboard(): Unit = TODO()
}