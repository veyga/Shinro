package io.astefanich.shinro.util

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion

//@BindingConversion
//internal fun solvedStatus(status: Boolean) = if (status) "COMPLETE" else "INCOMPLETE"

@BindingAdapter("solvedStatus")
fun setText(view: View, status: Boolean): String = if (status) "COMPLETE" else "INCOMPLETE"
