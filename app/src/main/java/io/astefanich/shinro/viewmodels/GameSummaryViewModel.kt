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
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.GameSummary
import io.astefanich.shinro.common.Metric
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
            launch {
                updateAchievements(newAggregate = targetDiffAgg)
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

    private fun updateAchievements(newAggregate: ResultAggregate){
//        withTimeout(pointsEarned.value!! / 2L){
        when (achievementsClient) {
            is Some -> {
                achievementsClient.t.load(true).addOnSuccessListener {
                    Timber.i("onsuccess loading buffer")
                    updateAchievements(achievementsClient.t, it.get(), newAggregate)
                }
            }
        }
    }


//        val updateAchievementsCB =
//        { client: AchievementsClient,
//          buffer: AchievementBuffer?,
//          aggregate: ResultAggregate ->
//            {

    private fun updateAchievements(client: AchievementsClient, buffer: AchievementBuffer?, aggregate: ResultAggregate){
            val winAchievements = setOf(
                Triple(Difficulty.EASY, 10, Metric.Achievement.Wins.Easy._10),
                Triple(Difficulty.EASY, 25, Metric.Achievement.Wins.Easy._25),
                Triple(Difficulty.EASY, 50, Metric.Achievement.Wins.Easy._50),
                Triple(Difficulty.MEDIUM, 10, Metric.Achievement.Wins.Medium._10),
                Triple(Difficulty.MEDIUM, 20, Metric.Achievement.Wins.Medium._25),
                Triple(Difficulty.MEDIUM, 50, Metric.Achievement.Wins.Medium._50),
                Triple(Difficulty.HARD, 10, Metric.Achievement.Wins.Hard._10),
                Triple(Difficulty.HARD, 25, Metric.Achievement.Wins.Hard._25),
                Triple(Difficulty.HARD, 50, Metric.Achievement.Wins.Hard._50),
            )
                .filter { aggregate.difficulty == it.first && aggregate.numWins >= it.second }
                .map { metricStr(it.third) }

            val timeAchievements = setOf(
                Triple(Difficulty.EASY, 5, Metric.Achievement.Time.Easy._5Min),
                Triple(Difficulty.EASY, 3, Metric.Achievement.Time.Easy._3Min),
                Triple(Difficulty.MEDIUM, 10, Metric.Achievement.Time.Medium._10Min),
                Triple(Difficulty.MEDIUM, 6, Metric.Achievement.Time.Medium._6Min),
                Triple(Difficulty.HARD, 20, Metric.Achievement.Time.Hard._20Min),
                Triple(Difficulty.HARD, 10, Metric.Achievement.Time.Hard._10Min),
            )
                .filter { summary.difficulty == it.first && summary.timeTaken <= (it.second * 60) }
                .map { metricStr(it.third) }


            buffer?.iterator()?.let {
                while (it.hasNext()) {
                    val achievement = it.next()
                    if (achievement.state != Achievement.STATE_UNLOCKED) {
                        val achId = achievement.achievementId
                        if (achId in winAchievements || achId in timeAchievements) {
                            Timber.i("unlocking $achId")
                            client.unlock(achId)
                        }
                    } else{
                        Timber.i("${achievement.achievementId} already unlocked")
                    }
                }
            }
        }

    }

