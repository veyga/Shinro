package io.astefanich.shinro.util

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.astefanich.shinro.R
import io.astefanich.shinro.domain.Instruction
import io.astefanich.shinro.domain.InstructionType

@BindingAdapter("completionStatus")
fun TextView.setCompletionStatus(status: Boolean) {
    text = if (status) "COMPLETE" else "INCOMPLETE"
}

@BindingAdapter("hideIfFalse")
fun hideViewIfFalseCondition(view: View, isActive: Boolean) = when (isActive) {
    true -> view.visibility = View.VISIBLE
    else -> view.visibility = View.GONE
}

@BindingAdapter("videoPlaying")
fun setVideoButtonText(button: Button, isPlaying: Boolean) = when (isPlaying) {
    false -> button.setText(R.string.what_is_shinro)
    true -> button.setText(R.string.pause_video)
}

@BindingAdapter("instructionText")
fun setInstructionTest(view: TextView, str: String) = when (str) {
    "PATHFINDER" -> view.setText(R.string.blocker_text)
    "BLOCKER" -> view.setText(R.string.blocker_text)
    "PIGEONHOLE" -> view.setText(R.string.pigeonhole_text)
    else -> view.setText(R.string.how_to_play_title)
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


@BindingAdapter("instructionImage")
fun bindInstructionImage(view: ImageView, instruction: Instruction) {


    fun bindGeneral(step: Int): Int = when (step) {
        1 -> R.drawable.ic_general_01
        else -> R.drawable.ic_general_02
    }

    fun bindBlocker(step: Int): Int = when (step) {
        1 -> R.drawable.ic_general_01
        else -> R.drawable.ic_general_02
    }

    fun bindPigeonhole(step: Int): Int = when (step) {
        1 -> R.drawable.ic_general_01
        else -> R.drawable.ic_general_02
    }

    fun bindPathfinder(step: Int): Int = when (step) {
        1 -> R.drawable.ic_general_01
        else -> R.drawable.ic_general_02
    }

    val resource = when (instruction.type) {
        InstructionType.PIGEONHOLE -> bindPigeonhole(instruction.stepNum)
        InstructionType.PATHFINDER -> bindPathfinder(instruction.stepNum)
        InstructionType.BLOCKER -> bindBlocker(instruction.stepNum)
        else -> bindGeneral(instruction.stepNum)
    }

    view.setImageResource(resource)
}


