package io.astefanich.shinro.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("completionStatus")
fun TextView.setCompletionStatus(status: Boolean) {
    text = if (status) "COMPLETE" else "INCOMPLETE"
}

@BindingAdapter("undoStackStatus")
fun bindUndoStackStatus(view: TextView, status: Boolean) {
    if (status)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}




