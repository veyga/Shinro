package io.astefanich.shinro.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import arrow.core.Some
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.LeaderboardsClient
import com.google.android.gms.games.achievement.Achievement
import com.google.android.gms.games.achievement.AchievementBuffer
import io.astefanich.shinro.common.*
import io.astefanich.shinro.model.ResultAggregate
import io.astefanich.shinro.model.plus
import io.astefanich.shinro.repository.ResultsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToLong


class GameSummaryViewModel
@Inject
constructor(
    val summary: GameSummary,
    val resultsRepo: ResultsRepository,
    val prefs: SharedPreferences,
    val leaderboardsClient: @JvmSuppressWildcards Option<LeaderboardsClient>,
    val achievementsClient: @JvmSuppressWildcards Option<AchievementsClient>,
    val metricStr: @JvmSuppressWildcards (Metric) -> String,
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
        //save/publish results
        viewModelScope.launch(Dispatchers.IO) {
            val gameAsAgg = summary.toResultAggregate()
            val targetDiffAgg = resultsRepo.getAggregateForDifficulty(summary.difficulty) + gameAsAgg
            val anyDiffAgg = resultsRepo.getAggregateForDifficulty(Difficulty.ANY) + gameAsAgg
            resultsRepo.updateAggregate(targetDiffAgg)
            resultsRepo.updateAggregate(anyDiffAgg)
            if(summary.isWin){
                launch {
                    updateAchievements(newAggregate = targetDiffAgg)
                }
            }
            launch {
                updateLeaderboard(newAggregate = targetDiffAgg)
            }
        }
    }


    fun changeDifficulty(newDiff: Difficulty) {
        _nextGameDifficulty.value = newDiff
    }

    private fun updateLeaderboard(newAggregate: ResultAggregate) {
        val newTotal = prefs.getLong("total_points", 0) + pointsEarned.value!!
        prefs.edit().putLong("total_points", newTotal).apply() //save total even if offline
        when (leaderboardsClient) {
            is Some -> {
                leaderboardsClient.t.submitScore(metricStr(Metric.Leaderboard.TotalPoints), newTotal)
                with(newAggregate) {
                    Timber.i("num played for ${difficulty} is currently $numPlayed") //min 10 plays before posting
                    if (numPlayed >= 10 && difficulty != Difficulty.EASY) {
                        val metric = if (difficulty == Difficulty.MEDIUM) Metric.Leaderboard.WinPctMedium else Metric.Leaderboard.WinPctHard
                        val newWinRate = ((numWins * 100f) / numPlayed).roundToLong()
                        leaderboardsClient.t.submitScore(metricStr(metric), newWinRate)
                    }
                }
            }
        }
    }

    private fun updateAchievements(newAggregate: ResultAggregate) {
//        withTimeout(pointsEarned.value!! / 2L){
        Timber.i("updating achievements")
        when (achievementsClient) {
            is Some -> {
                achievementsClient.t.load(true).addOnSuccessListener {
                    Timber.i("onsuccess loading buffer")
                    updateAchievements(achievementsClient.t, it.get(), newAggregate)
                }
            }
        }
    }

    private fun updateAchievements(client: AchievementsClient, buffer: AchievementBuffer?, aggregate: ResultAggregate) {

        val winAchievementsForGame = WIN_ACHIEVEMENTS
            .filter { aggregate.difficulty == it.difficulty && aggregate.numWins >= it.count }
            .map { metricStr(it) }

        val timeAchievementsForGame = TIME_ACHIEVEMENTS
            .filter { aggregate.difficulty == it.difficulty && summary.timeTaken <= it.timeLimit }
            .map { metricStr(it) }

        buffer?.iterator()?.let {
            while (it.hasNext()) {
                val achievement = it.next()
                Timber.i("checking ${achievement.description}")
                val achId = achievement.achievementId
                if (achId in winAchievementsForGame || achId in timeAchievementsForGame) {
                    Timber.i("trying to unlock ${achId}")
                    if (achievement.state != Achievement.STATE_UNLOCKED) {
                        Timber.i("unlocking ${achievement.description}")
                        client.unlock(achId)
                    }
                }
            }
        }
    }
}

