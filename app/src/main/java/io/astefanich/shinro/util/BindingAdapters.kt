package io.astefanich.shinro.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("completionStatus")
fun TextView.setCompletionStatus(status: Boolean) {
    text = if (status) "COMPLETE" else "INCOMPLETE"
}

@BindingAdapter("undoStackActive")
fun bindUndoStackStatus(view: TextView, isActive: Boolean) = when (isActive) {
    true -> view.visibility = View.VISIBLE
    else -> view.visibility = View.GONE
}




