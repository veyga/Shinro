package io.astefanich.shinro.common

import io.astefanich.shinro.R

sealed class Metric(val difficulty: Difficulty, val id: Int) {

    sealed class Leaderboard(difficulty: Difficulty, id: Int) : Metric(difficulty, id) {

        object TotalPoints : Leaderboard(Difficulty.ANY, R.string.leaderboard_total_points)
        object WinPctMedium : Leaderboard(Difficulty.MEDIUM, R.string.leaderboard_win__medium_min_10_games)
        object WinPctHard : Leaderboard(Difficulty.HARD, R.string.leaderboard_win__hard_min_10_games)
    }

    sealed class Achievement(difficulty: Difficulty, id: Int) : Metric(difficulty, id) {

        sealed class Wins(val count: Int, difficulty: Difficulty, id: Int) : Achievement(difficulty, id) {

            sealed class Easy(count: Int, id: Int) : Wins(count, Difficulty.EASY, id) {
                object _10 : Easy(10, R.string.achievement_10_easy)
                object _25 : Easy(25, R.string.achievement_25_easy)
                object _50 : Easy(50, R.string.achievement_50_easy)
            }

            sealed class Medium(count: Int, id: Int) : Wins(count, Difficulty.MEDIUM, id) {
                object _10 : Medium(10, R.string.achievement_10_medium)
                object _25 : Medium(25, R.string.achievement_25_medium)
                object _50 : Medium(50, R.string.achievement_50_medium)
            }

            sealed class Hard(count: Int, id: Int) : Wins(count, Difficulty.HARD, id) {
                object _10 : Hard(10, R.string.achievement_10_hard)
                object _25 : Hard(25, R.string.achievement_25_hard)
                object _50 : Hard(50, R.string.achievement_50_hard)
            }
        }

        sealed class Time(val timeLimit: Long, difficulty: Difficulty, id: Int) : Achievement(difficulty, id) {

            sealed class Easy(timeLimit: Long, id: Int) : Time(timeLimit, Difficulty.EASY, id) {
                object _5Min : Easy(5 * 60, R.string.achievement_5_minute_easy)
                object _3Min : Easy(3 * 60, R.string.achievement_3_minute_easy)
            }

            sealed class Medium(timeLimit: Long, id: Int) : Time(timeLimit, Difficulty.MEDIUM, id) {
                object _10Min : Medium(10 * 60, R.string.achievement_10_minute_medium)
                object _6Min : Medium(6 * 60, R.string.achievement_6_minute_medium)
            }

            sealed class Hard(timeLimit: Long, id: Int) : Time(timeLimit, Difficulty.HARD, id) {
                object _20Min : Hard(20 * 60, R.string.achievement_20_minute_hard)
                object _10Min : Hard(10 * 60, R.string.achievement_10_minute_hard)
            }
        }
    }
}

val WIN_ACHIEVEMENTS = setOf(
    Metric.Achievement.Wins.Easy._10,
    Metric.Achievement.Wins.Easy._25,
    Metric.Achievement.Wins.Easy._50,
    Metric.Achievement.Wins.Medium._10,
    Metric.Achievement.Wins.Medium._25,
    Metric.Achievement.Wins.Medium._50,
    Metric.Achievement.Wins.Hard._10,
    Metric.Achievement.Wins.Hard._25,
    Metric.Achievement.Wins.Hard._50,
)

val TIME_ACHIEVEMENTS = setOf(
    Metric.Achievement.Time.Easy._5Min,
    Metric.Achievement.Time.Easy._3Min,
    Metric.Achievement.Time.Medium._10Min,
    Metric.Achievement.Time.Medium._6Min,
    Metric.Achievement.Time.Hard._20Min,
    Metric.Achievement.Time.Hard._10Min,
)
