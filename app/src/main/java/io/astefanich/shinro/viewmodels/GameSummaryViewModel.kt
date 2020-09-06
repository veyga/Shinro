package io.astefanich.shinro.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.LeaderboardsClient
import com.google.android.gms.games.achievement.Achievement
import io.astefanich.shinro.common.*
import io.astefanich.shinro.model.ResultAggregate
import io.astefanich.shinro.model.plus
import io.astefanich.shinro.repository.ResultsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.math.roundToLong


class GameSummaryViewModel
@Inject
constructor(
    val summary: GameSummary,
    val resultsRepo: ResultsRepository,
    val prefs: SharedPreferences,
    val networkTimeout: NetworkTimeout,
    val leaderboardsClient: @JvmSuppressWildcards Option<LeaderboardsClient>,
    val achievementsClient: @JvmSuppressWildcards Option<AchievementsClient>,
    val metricStr: @JvmSuppressWildcards (Metric) -> String,
    val calculateScore: @JvmSuppressWildcards (Difficulty, Long) -> Int
) : ViewModel() {


    private var _nextGameDifficulty = MutableLiveData<Difficulty>()
    val nextGameDifficulty: LiveData<Difficulty>
        get() = _nextGameDifficulty

    val pointsEarned = MutableLiveData<Int>()

    val scoresPublished = MutableLiveData<Boolean>()

    init {
        with(summary) {
            _nextGameDifficulty.value = difficulty
            pointsEarned.value =
                if (summary.isWin) calculateScore(difficulty, timeTaken) else 0
        }
        //save/publish results
        viewModelScope.launch(Dispatchers.IO) {
            val gameAsAgg = summary.toResultAggregate()
            val targetDiffAgg = resultsRepo.getAggregateForDifficulty(summary.difficulty) + gameAsAgg
            val anyDiffAgg = resultsRepo.getAggregateForDifficulty(Difficulty.ANY) + gameAsAgg
            resultsRepo.updateAggregate(targetDiffAgg)
            resultsRepo.updateAggregate(anyDiffAgg)
            val saveResult = when (Pair(leaderboardsClient, achievementsClient)) {
                Pair(None, None) -> null
                else -> withTimeoutOrNull(networkTimeout.ms) {
                    updateLeaderboard(newAggregate = targetDiffAgg)
                    if (summary.isWin)
                        updateAchievements(newAggregate = targetDiffAgg)
                    true
                }
            }
            scoresPublished.postValue(saveResult?.and(true) ?: false)
        }
    }


    fun changeDifficulty(newDiff: Difficulty) {
        _nextGameDifficulty.value = newDiff
    }

    private suspend fun updateLeaderboard(newAggregate: ResultAggregate) {
        val newTotal = prefs.getLong("total_points", 0) + pointsEarned.value!!
        prefs.edit().putLong("total_points", newTotal).apply() //save total even if offline
        when (leaderboardsClient) {
            is Some -> {
                if(newTotal > 0)
                    leaderboardsClient.t.submitScore(metricStr(Metric.Leaderboard.TotalPoints), newTotal)
                with(newAggregate) {
                    if (numPlayed >= 10 && difficulty != Difficulty.EASY) {
                        val metric =
                            if (difficulty == Difficulty.MEDIUM) Metric.Leaderboard.WinPctMedium else Metric.Leaderboard.WinPctHard
                        val newWinRate = ((numWins * 100f) / numPlayed).roundToLong()
                        leaderboardsClient.t.submitScore(metricStr(metric), newWinRate)
                    }
                }
            }
        }
    }

    private suspend fun updateAchievements(newAggregate: ResultAggregate) {
        when (achievementsClient) {
            is Some -> {
                achievementsClient.t.load(true).addOnSuccessListener { achievementsBuffer ->
                    val winAchievementsForGame = WIN_ACHIEVEMENTS
                        .filter { newAggregate.difficulty == it.difficulty && newAggregate.numWins >= it.count }
                        .map { metricStr(it) }

                    val timeAchievementsForGame = TIME_ACHIEVEMENTS
                        .filter { newAggregate.difficulty == it.difficulty && summary.timeTaken <= it.timeLimit }
                        .map { metricStr(it) }

                    achievementsBuffer.get()?.iterator()?.let { iter ->
                        while (iter.hasNext()) {
                            val achievement = iter.next()
                            val achId = achievement.achievementId
                            if (achId in winAchievementsForGame || achId in timeAchievementsForGame) {
                                if (achievement.state != Achievement.STATE_UNLOCKED) {
                                    achievementsClient.t.unlock(achId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

