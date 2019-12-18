package io.astefanich.shinro.util

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("completionStatus")
fun TextView.setCompletionStatus(status: Boolean){
    text = if (status) "COMPLETE" else "INCOMPLETE"
}





