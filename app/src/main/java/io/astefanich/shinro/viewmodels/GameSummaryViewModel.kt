package io.astefanich.shinro.viewmodels

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import arrow.core.Some
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.LeaderboardsClient
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
    val rez: Resources,
    val leaderboardsClient: @JvmSuppressWildcards Option<LeaderboardsClient>,
    val achievementsClient: @JvmSuppressWildcards Option<AchievementsClient>,
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
            val targetDiffAgg =
                resultsRepo.getAggregateForDifficulty(summary.difficulty) + gameAsAgg
            val anyDiffAgg = resultsRepo.getAggregateForDifficulty(Difficulty.ANY) + gameAsAgg
            resultsRepo.updateAggregate(targetDiffAgg)
            resultsRepo.updateAggregate(anyDiffAgg)
            when (leaderboardsClient) {
                is Some -> {
                    publishUpdatedWinRate(leaderboardsClient.t, targetDiffAgg)
                }
            }
            if (summary.isWin) {
                val newTotal = recordNewTotal()
                when (leaderboardsClient) {
                    is Some -> leaderboardsClient.t.submitScore(
                        rez.getString(Metric.Leaderboard.TotalPoints.id),
                        newTotal
                    )
                }
                when (achievementsClient) {
                    is Some -> publishAchievements(achievementsClient.t, targetDiffAgg)
                }
            }
        }
    }

    fun changeDifficulty(newDiff: Difficulty) {
        _nextGameDifficulty.value = newDiff
    }

    private suspend fun recordNewTotal(): Long {
        //save total points when offline
        val currentTotal = prefs.getLong("total_points", 0)
        Timber.i("currentTotal is $currentTotal")
        val newTotal = currentTotal + pointsEarned.value!!
        Timber.i("newTotal is $newTotal")
        prefs.edit().putLong("total_points", newTotal).apply()
        return newTotal
    }

    private suspend fun publishUpdatedWinRate(
        client: LeaderboardsClient,
        aggregate: ResultAggregate
    ) {
        //min 10 plays before posting
        with(aggregate) {
            Timber.i("num played for ${difficulty} is currently $numPlayed")
            if (numPlayed >= 10 && difficulty != Difficulty.EASY) {
                val stat = if (difficulty == Difficulty.MEDIUM)
                    Metric.Leaderboard.WinPctMedium else Metric.Leaderboard.WinPctHard
                val newWinRate = ((numWins * 100f) / numPlayed).roundToLong()
                client.submitScore(rez.getString(stat.id), newWinRate)
            }
        }
    }


    private suspend fun publishAchievements(
        client: AchievementsClient,
        aggregate: ResultAggregate
    ) {

        //cant change achievement after publishing! ideally would've used incremental achievement
        //publish win achievement
        val buffer10Wins = 10..24
        val buffer25Wins = 25..49
        val buffer50Wins = 50..99
        listOf(
            Triple(Difficulty.EASY, buffer10Wins, Metric.Achievement.Wins.Easy._10),
            Triple(Difficulty.EASY, buffer25Wins, Metric.Achievement.Wins.Easy._25),
            Triple(Difficulty.EASY, buffer50Wins, Metric.Achievement.Wins.Easy._50),
            Triple(Difficulty.MEDIUM, buffer10Wins, Metric.Achievement.Wins.Medium._10),
            Triple(Difficulty.MEDIUM, buffer25Wins, Metric.Achievement.Wins.Medium._25),
            Triple(Difficulty.MEDIUM, buffer50Wins, Metric.Achievement.Wins.Medium._50),
            Triple(Difficulty.HARD, buffer10Wins, Metric.Achievement.Wins.Hard._10),
            Triple(Difficulty.HARD, buffer25Wins, Metric.Achievement.Wins.Hard._25),
            Triple(Difficulty.HARD, buffer50Wins, Metric.Achievement.Wins.Hard._50),
        )
            .filter { aggregate.difficulty == it.first && aggregate.numWins in it.second }
            .map { rez.getString(it.third.id) }
            .forEach { Timber.i("you've unlocked $it"); client.unlock(it) }


        //publish time achievement
        listOf(
            Triple(Difficulty.EASY, 5, Metric.Achievement.Time.Easy._5Min),
            Triple(Difficulty.EASY, 3, Metric.Achievement.Time.Easy._3Min),
            Triple(Difficulty.MEDIUM, 10, Metric.Achievement.Time.Medium._10Min),
            Triple(Difficulty.MEDIUM, 6, Metric.Achievement.Time.Medium._6Min),
            Triple(Difficulty.HARD, 20, Metric.Achievement.Time.Hard._20Min),
            Triple(Difficulty.HARD, 10, Metric.Achievement.Time.Hard._10Min),
        )
            .filter { summary.difficulty == it.first }
            .takeWhile { summary.timeTaken <= (it.second * 60) }
            .map { rez.getString(it.third.id) }
            .forEach { Timber.i("you've unlocked $it"); client.unlock(it) }
    }


}