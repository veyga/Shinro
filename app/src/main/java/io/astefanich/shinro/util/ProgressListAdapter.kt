package io.astefanich.shinro.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.ListItemProgressBinding
import io.astefanich.shinro.domain.Progress

class ProgressAdapter(val clickListener: ProgressClickListener) :
    ListAdapter<Progress, ProgressAdapter.ProgressViewHolder>(ProgressDiffCallback()) {

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        return ProgressViewHolder.from(parent)
    }

    class ProgressViewHolder(val binding: ListItemProgressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ProgressClickListener, progressItem: Progress) {
            binding.progressItem = progressItem
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ProgressViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemProgressBinding.inflate(layoutInflater, parent, false)
                val view = layoutInflater
                    .inflate(R.layout.list_item_progress, parent, false)
                return ProgressViewHolder(binding)
            }
        }

    }
}


class ProgressClickListener(val fn: (boardNum: Int) -> Unit) {
    fun onClick(progress: Progress) = fn(progress.boardNum)
}

class ProgressDiffCallback : DiffUtil.ItemCallback<Progress>() {
    override fun areItemsTheSame(oldItem: Progress, newItem: Progress): Boolean {
        return oldItem.boardNum == newItem.boardNum
    }

    override fun areContentsTheSame(oldItem: Progress, newItem: Progress): Boolean {
        return oldItem == newItem
    }
}
