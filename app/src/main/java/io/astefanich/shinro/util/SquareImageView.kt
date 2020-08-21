package io.astefanich.shinro.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.updatePadding
import io.astefanich.shinro.R

/**
 * An ImageView that re-sizes itself.
 */
class SquareImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }


    fun bindSvg(str: String){
        val res = when (str) {
            "Q" -> R.drawable.ic_blank_cell  //cell 0,0
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
        setImageResource(res)
        val isDiagonal = str in setOf("B", "D", "F", "H")
        //TODO use DP not px?  Would need the context
        val px = 12
        if (isDiagonal)
            updatePadding(top = px, bottom = px, left = px, right = px)
    }
}
