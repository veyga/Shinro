package io.astefanich.shinro.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Cell(var current: String, val actual: String = current)

typealias Grid = Array<Array<Cell>>

enum class Difficulty { EASY, MEDIUM, HARD }

//// between gameVM and gameOver
@Parcelize
data class GameSummary(val difficulty: Difficulty, val win: Boolean, val time: Long) : Parcelable

sealed class PlayRequest : Parcelable {
    @Parcelize
    object Resume : PlayRequest()

    @Parcelize
    data class NewGame(val difficulty: Difficulty) : PlayRequest()
}

@Parcelize
enum class TipChoice : Parcelable { HOWTOPLAY, PATHFINDER, BLOCKER, PIGEONHOLE }

data class Tip(val drawable: Int, val text: String)


