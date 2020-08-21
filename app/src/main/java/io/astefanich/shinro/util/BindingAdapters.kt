package io.astefanich.shinro.util

import android.animation.ValueAnimator
import android.text.format.DateUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import io.astefanich.shinro.R
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.TipChoice
import io.astefanich.shinro.ui.SquareImageView
import timber.log.Timber

@BindingAdapter("winLoss")
fun TextView.setWinLossString(status: Boolean) {
    val (txt, color) = if (status)
        Pair(resources.getString(R.string.victory), resources.getColor(R.color.green))
    else Pair(resources.getString(R.string.defeat), resources.getColor(R.color.red))
    text = txt.toUpperCase()
    setTextColor(color)
}

@BindingAdapter("hideIfFalse")
fun hideViewIfFalseCondition(view: View, isActive: Boolean) = when (isActive) {
    true -> view.visibility = View.VISIBLE
    else -> view.visibility = View.INVISIBLE
}

@BindingAdapter("setResetCheckpointText")
fun TextView.toggleSetCheckpointText(isActive: Boolean) {
    Timber.i("its active? $isActive")
    val stringId = if (isActive) R.string.reset_checkpoint else R.string.set_checkpoint
    text = resources.getString(stringId)
}

@BindingAdapter("difficultyAnim", "pointsEarned")
fun TextView.animatePointsEarned(difficulty: Difficulty, score: Int) {
    val scoreAnimTimes =
        mapOf(
            Difficulty.EASY to 1000L,
            Difficulty.MEDIUM to 2500L,
            Difficulty.HARD to 4000L
        )
    val delay = 1000L
    ValueAnimator.ofInt(0, score).apply {
        addUpdateListener {
            text = String.format(
                resources.getString(
                    R.string.points_earned_fmt,
                    it.animatedValue as Int
                )
            )
        }
        duration = delay + scoreAnimTimes.get(difficulty)!!
        startDelay = delay
        start()
    }
}

@BindingAdapter("videoPlaying", "videoStarted")
fun setVideoButtonText(button: Button, isPlaying: Boolean, isStarted: Boolean) {
    if (!isStarted) {
        button.setText(R.string.what_is_shinro)
    } else {
        if (isPlaying) {
            button.setText(R.string.pause_video)
        } else {
            button.setText(R.string.resume_video)
        }
    }
}

//@BindingAdapter("progressFormatted")
//fun Chip.setProgressChip(item: Progress) {
//    item?.let {
//        text = String.format(
//            resources.getString(R.string.progress_fmt),
//            item.boardNum,
//            item.difficulty
//        )
//        typeface = Typeface.DEFAULT_BOLD
//        setChipIconResource(if (item.completed) R.drawable.checkmark else R.drawable.delete)
//    }
//}

//@BindingAdapter("timer")
//fun TextView.displayTimer(time: Long) {
//    text = String.format(resources.getString(R.string.timer_fmt), DateUtils.formatElapsedTime(time))
//}

@BindingAdapter("timeTaken")
fun TextView.displayTimeTaken(time: Long) {
    val fmt = "Time: %s"
    text = String.format(
        resources.getString(R.string.time_taken_fmt),
        DateUtils.formatElapsedTime(time)
    )
}

@BindingAdapter("difficultyText")
fun TextView.setDifficultyText(difficulty: Difficulty?) {
    val repr =
        if (difficulty == Difficulty.EASY) "Easy"
        else if (difficulty == Difficulty.MEDIUM) "Medium"
        else "Hard"
    text = repr
}

@BindingAdapter("difficultyIcon")
fun ImageView.setDifficultyIcon(difficulty: Difficulty?) {
    val drawable =
        if (difficulty == Difficulty.EASY) R.drawable.ic_green_circle32
        else if (difficulty == Difficulty.MEDIUM) R.drawable.ic_blue_square32
        else R.drawable.ic_gray_diamond32


    setImageDrawable(resources.getDrawable(drawable))
}

@BindingAdapter("gridSvg")
fun bindGridSvg(view: SquareImageView, str: String?) {
    val res = when (str) {
        " " -> R.drawable.ic_blank_cell
        "X" -> R.drawable.ic_letter_x
        "M" -> R.drawable.ic_circle_red
        "0" -> R.drawable.ic_number0
        "1" -> R.drawable.ic_number1
        "2" -> R.drawable.ic_number2
        "3" -> R.drawable.ic_number3
        "4" -> R.drawable.ic_number4
        "5" -> R.drawable.ic_number5
        "6" -> R.drawable.ic_number6
        "7" -> R.drawable.ic_number7
        "A" -> R.drawable.ic_arrow_up
        "B" -> R.drawable.ic_arrow_up_right
        "C" -> R.drawable.ic_arrow_right
        "D" -> R.drawable.ic_arrow_down_right
        "E" -> R.drawable.ic_arrow_down
        "F" -> R.drawable.ic_arrow_down_left
        "G" -> R.drawable.ic_arrow_left
        "H" -> R.drawable.ic_arrow_up_left
        else -> R.drawable.ic_blank_cell
    }
    view.setImageResource(res)
    val isDiagonal = str in setOf("B", "D", "F", "H")
    //TODO use DP not px?  Would need the context
    val px = 12
    if (isDiagonal)
        view.updatePadding(top = px, bottom = px, left = px, right = px)

}


//The view doesn't align correctly (vertically) across the different types/screen resolutions..
@BindingAdapter("tipText")
fun setTipText(view: TextView, type: TipChoice) {
    view.setText("")
}

//@BindingAdapter("tipText")
//fun setTipText(view: TextView, type: TipChoice) = when (type) {
//    TipChoice.PATHFINDER -> view.setText(R.string.pathfinder)
//    TipChoice.BLOCKER -> view.setText(R.string.blocker)
//    TipChoice.PIGEONHOLE -> view.setText(R.string.pigeonhole)
//    else -> view.setText(R.string.how_to_play_title)
//}



