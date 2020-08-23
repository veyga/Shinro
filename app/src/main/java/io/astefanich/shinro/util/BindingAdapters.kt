package io.astefanich.shinro.util

import android.animation.ValueAnimator
import android.text.format.DateUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.astefanich.shinro.R
import io.astefanich.shinro.common.TipChoice

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

//@BindingAdapter("pointsEarned")
//fun TextView.animatePointsEarned(score: Int) {
//    val delay = 1000L
//    ValueAnimator.ofInt(0, score).apply {
//        addUpdateListener {
//            text = String.format(
//                resources.getString(
//                    R.string.points_earned_fmt,
//                    it.animatedValue as Int
//                )
//            )
//        }
//        duration = delay + score / 2
//        startDelay = delay
//        start()
//    }
//}

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

@BindingAdapter("timeTaken")
fun TextView.displayTimeTaken(time: Long) {
    val fmt = "Time: %s"
    text = String.format(
        resources.getString(R.string.time_taken_fmt),
        DateUtils.formatElapsedTime(time)
    )
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



