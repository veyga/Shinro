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
                is Some -> publishUpdatedWinRate(leaderboardsClient.t, targetDiffAgg)
            }
            if (summary.isWin) {
                when (leaderboardsClient) {
                    is Some -> publishNewScore(leaderboardsClient.t)
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

    private suspend fun publishNewScore(client: LeaderboardsClient) {
        val currentTotal = prefs.getLong("total_points", 0)
        Timber.i("currentTotal is $currentTotal")
        val newTotal = currentTotal + pointsEarned.value!!
        Timber.i("newTotal is $newTotal")
        prefs.edit().putLong("total_points", newTotal).apply()
        client.submitScore(rez.getString(Metric.Leaderboard.TotalPoints.id), newTotal)
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
        fun publishWins() {
            with(aggregate) {
                when (difficulty) {
                    Difficulty.EASY -> {
                        //unlocking multiple times?
                        if (numWins >= 10)
                            client.unlock(rez.getString(Metric.Achievement.Wins.Easy._10.id))
                        if (numWins >= 25)
                            client.unlock(rez.getString(Metric.Achievement.Wins.Easy._25.id))
                        if (numWins >= 50)
                            client.unlock(rez.getString(Metric.Achievement.Wins.Easy._50.id))

                    }
                    Difficulty.MEDIUM -> {
                        if (numWins >= 10)
                            client.unlock(rez.getString(Metric.Achievement.Wins.Medium._10.id))
                        if (numWins >= 25)
                            client.unlock(rez.getString(Metric.Achievement.Wins.Medium._25.id))
                        if (numWins >= 50)
                            client.unlock(rez.getString(Metric.Achievement.Wins.Medium._50.id))
                    }
                    Difficulty.HARD -> {
                        if (numWins >= 10)
                            client.unlock(rez.getString(Metric.Achievement.Wins.Hard._10.id))
                        if (numWins >= 25)
                            client.unlock(rez.getString(Metric.Achievement.Wins.Hard._25.id))
                        if (numWins >= 50)
                            client.unlock(rez.getString(Metric.Achievement.Wins.Hard._50.id))
                    }
                }
            }
        }

        fun publishTimeAchievements() {
            with(aggregate) {
                val timeIds = when (difficulty) {
                    Difficulty.EASY -> {
                        val ids = mutableSetOf<Metric>()
                        if (summary.timeTaken <= 5 * 60)
                            ids.add(Metric.Achievement.Time.Easy._5Min)
                        if (summary.timeTaken <= 3 * 60) {
                            ids.add(Metric.Achievement.Time.Easy._3Min)
                        }
                        ids
                    }
                    Difficulty.MEDIUM -> {
                        val ids = mutableSetOf<Metric>()
                        if (summary.timeTaken <= 10 * 60)
                            ids.add(Metric.Achievement.Time.Medium._10Min)
                        if (summary.timeTaken <= 6 * 60) {
                            ids.add(Metric.Achievement.Time.Medium._6Min)
                        }
                        ids
                    }
                    else -> {
                        val ids = mutableSetOf<Metric>()
                        if (summary.timeTaken <= 20 * 60)
                            ids.add(Metric.Achievement.Time.Hard._20Min)
                        if (summary.timeTaken <= 10 * 60) {
                            ids.add(Metric.Achievement.Time.Hard._10Min)
                        }
                        ids
                    }
                }
                timeIds.map { rez.getString(it.id) }.forEach { client.unlock(it) }
            }
        }

        publishWins()
        publishTimeAchievements()
    }

}