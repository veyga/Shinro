package io.astefanich.shinro.util

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.astefanich.shinro.R
import io.astefanich.shinro.domain.Progress
import kotlinx.android.synthetic.main.list_item_progress.view.*

class ProgressAdapter : ListAdapter<Progress, ProgressAdapter.ViewHolder>(ProgressDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_progress, parent, false)

                return ViewHolder(view)
            }
        }

        fun bind(progressItem: Progress) {
            itemView.apply {
//                progress_item_chip.text =
//                    "Puzzle ${progressItem.boardId}\t(${progressItem.difficulty})      "
                progress_item_chip.text =
                    "Puzzle 1\t(${progressItem.difficulty})      "
                progress_item_chip.typeface = Typeface.DEFAULT_BOLD
                val icon = if (progressItem.completed) R.drawable.checkmark else R.drawable.delete
                progress_item_chip.setChipIconResource(icon)
//                progress_item_chip.setOnClickListener {
//                    findNavController().navigate(
//                        ProgressListFragmentDirections.actionProgressToGame(progressItem.boardId)
//                    )
//                }
            }
        }
    }
}

class ProgressDiffCallback : DiffUtil.ItemCallback<Progress>() {
    override fun areItemsTheSame(oldItem: Progress, newItem: Progress): Boolean {
        return oldItem.completed == newItem.completed
//        return oldItem.boardId == newItem.boardId
    }

    override fun areContentsTheSame(oldItem: Progress, newItem: Progress): Boolean {
        return oldItem == newItem
    }
}
