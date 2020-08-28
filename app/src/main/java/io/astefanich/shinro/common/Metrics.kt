package io.astefanich.shinro.common

import io.astefanich.shinro.R

sealed class Metric(val id: Int) {

    sealed class Leaderboard(id: Int) : Metric(id) {
        object TotalPoints : Leaderboard(R.string.leaderboard_total_points)
        object WinPctMedium : Leaderboard(R.string.leaderboard_win__medium_min_10_games)
        object WinPctHard : Leaderboard(R.string.leaderboard_win__hard_min_10_games)
    }

    sealed class Achievement(id: Int) : Metric(id) {

        sealed class Wins(id: Int) : Achievement(id) {
            sealed class Easy(id: Int) : Wins(id) {
                object _10 : Easy(R.string.achievement_10_easy)
                object _25 : Easy(R.string.achievement_25_easy)
                object _50 : Easy(R.string.achievement_50_easy)
            }

            sealed class Medium(id: Int) : Wins(id) {
                object _10 : Medium(R.string.achievement_10_medium)
                object _25 : Medium(R.string.achievement_25_medium)
                object _50 : Medium(R.string.achievement_50_medium)
            }

            sealed class Hard(id: Int) : Wins(id) {
                object _10 : Hard(R.string.achievement_10_hard)
                object _25 : Hard(R.string.achievement_25_hard)
                object _50 : Hard(R.string.achievement_50_hard)
            }
        }

        sealed class Time(id: Int) : Achievement(id) {
            sealed class Easy(id: Int) : Time(id) {
                object _5Min : Easy(R.string.achievement_5_minute_easy)
                object _3Min : Easy(R.string.achievement_3_minute_easy)
            }

            sealed class Medium(id: Int) : Time(id) {
                object _10Min : Medium(R.string.achievement_10_minute_medium)
                object _6Min : Medium(R.string.achievement_6_minute_medium)
            }

            sealed class Hard(id: Int) : Time(id) {
                object _20Min : Hard(R.string.achievement_20_minute_hard)
                object _10Min : Hard(R.string.achievement_10_minute_hard)
            }
        }
    }
}
