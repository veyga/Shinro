package io.astefanich.shinro.common

import io.astefanich.shinro.R

sealed class Metric(val id: Int) {

    sealed class Leaderboard(id: Int) : Metric(id) {
        object TotalPoints : Leaderboard(R.string.leaderboard_total_points)
        object WinPctMedium : Leaderboard(R.string.leaderboard_winpct_medium)
        object WinPctHard : Leaderboard(R.string.leaderboard_winpct_hard)
    }

    sealed class Achievement(id: Int) : Metric(id) {

        sealed class Wins(id: Int) : Achievement(id) {
            sealed class Easy(id: Int) : Wins(id) {
                object _10 : Easy(R.string.achievement_10easy)
                object _25 : Easy(R.string.achievement_25easy)
                object _50 : Easy(R.string.achievement_50easy)
            }

            sealed class Medium(id: Int) : Wins(id) {
                object _10 : Medium(R.string.achievement_10easy)
                object _25 : Medium(R.string.achievement_25medium)
                object _50 : Medium(R.string.achievement_50medium)
            }

            sealed class Hard(id: Int) : Wins(id) {
                object _10 : Hard(R.string.achievement_10hard)
                object _25 : Hard(R.string.achievement_25hard)
                object _50 : Hard(R.string.achievement_50hard)
            }
        }

        sealed class Time(id: Int) : Achievement(id) {
            sealed class Easy(id: Int) : Time(id) {
                object _5Min : Easy(R.string.achievement_5mineasy)
                object _3Min : Easy(R.string.achievement_3mineasy)
            }

            sealed class Medium(id: Int) : Time(id) {
                object _10Min : Medium(R.string.achievement_10minmedium)
                object _6Min : Medium(R.string.achievement_6minmedium)
            }

            sealed class Hard(id: Int) : Time(id) {
                object _20Min : Hard(R.string.achievement_20minhard)
                object _10Min : Hard(R.string.achievement_10minhard)
            }
        }
    }
}
