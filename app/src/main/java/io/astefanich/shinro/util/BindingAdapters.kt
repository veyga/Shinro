package io.astefanich.shinro.util

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.astefanich.shinro.R
import io.astefanich.shinro.domain.InstructionType

@BindingAdapter("completionStatus")
fun TextView.setCompletionStatus(status: Boolean) {
    val (txt, color) = if (status)
        Pair("COMPLETE", resources.getColor(R.color.green))
    else Pair("INCOMPLETE", resources.getColor(R.color.red))
    text = txt
    setTextColor(color)
}

@BindingAdapter("hideIfFalse")
fun hideViewIfFalseCondition(view: View, isActive: Boolean) = when (isActive) {
    true -> view.visibility = View.VISIBLE
    else -> view.visibility = View.INVISIBLE
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

@BindingAdapter("gridSvg")
fun bindGridSvg(view: ImageView, str: String) {
    val res = when (str) {
        " " -> R.drawable.ic_blank_cell
        "X" -> R.drawable.ic_letter_x
        "M" -> R.drawable.ic_red_circle
        "0" -> R.drawable.ic_n0
        "1" -> R.drawable.ic_n1
        "2" -> R.drawable.ic_n2
        "3" -> R.drawable.ic_n3
        "4" -> R.drawable.ic_n4
        "5" -> R.drawable.ic_n5
        "6" -> R.drawable.ic_n6
        "7" -> R.drawable.ic_n7
        "A" -> R.drawable.ic_blue_up_arrow
        "B" -> R.drawable.ic_blue_right_up_arrow
        "C" -> R.drawable.ic_blue_right_arrow
        "D" -> R.drawable.ic_blue_right_down_arrow
        "E" -> R.drawable.ic_blue_down_arrow
        "F" -> R.drawable.ic_blue_left_down_arrow
        "G" -> R.drawable.ic_blue_left_arrow
        "H" -> R.drawable.ic_blue_left_up_arrow
        else -> R.drawable.ic_blank_cell
    }
    view.setImageResource(res)
}


//The view doesn't align correctly (vertically) across the different types/screen resolutions..
@BindingAdapter("instructionText")
fun setInstructionText(view: TextView, type: InstructionType) {
    view.setText("")
}
//@BindingAdapter("instructionText")
//fun setInstructionText(view: TextView, type: InstructionType) = when (type) {
//    InstructionType.PATHFINDER -> view.setText(R.string.pathfinder_text)
//    InstructionType.BLOCKER -> view.setText(R.string.blocker_text)
//    InstructionType.PIGEONHOLE -> view.setText(R.string.pigeonhole_text)
//    else -> view.setText(R.string.how_to_play_title)
//}



