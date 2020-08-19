package io.astefanich.shinro.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Cell(var current: String, val actual: String = current)

typealias Grid = Array<Array<Cell>>

enum class Difficulty { EASY, MEDIUM, HARD }

enum class TimePeriod(val seconds: Long) {
    ONE(1L),
    FIVE(5L),
    TEN(10L)
}

//// between gameVM and gameOver
@Parcelize
data class GameSummary(val difficulty: Difficulty, val isWin: Boolean, val time: Long) : Parcelable

sealed class PlayRequest : Parcelable {
    @Parcelize
    object Resume : PlayRequest()

    @Parcelize
    data class NewGame(val difficulty: Difficulty) : PlayRequest()
}

@Parcelize
enum class TipChoice : Parcelable { HOWTOPLAY, PATHFINDER, BLOCKER, PIGEONHOLE }

data class Tip(val drawable: Int, val text: String)


