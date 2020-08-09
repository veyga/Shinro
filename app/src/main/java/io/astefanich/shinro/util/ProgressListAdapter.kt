package io.astefanich.shinro.util

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.astefanich.shinro.R
import io.astefanich.shinro.domain.Progress
import io.astefanich.shinro.ui.ProgressListFragmentDirections
import kotlinx.android.synthetic.main.list_item_progress.view.*

class ProgressRecyclerAdapter(private val items: List<Progress>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProgressItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_progress,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProgressItemViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    internal class ProgressItemViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(progressItem: Progress) {
            itemView.apply {
                progress_item_chip.text = "Puzzle ${progressItem.boardId}\t(${progressItem.difficulty})      "
                progress_item_chip.typeface = Typeface.DEFAULT_BOLD
                val icon = if (progressItem.completed) R.drawable.checkmark else R.drawable.delete
                progress_item_chip.setChipIconResource(icon)
                progress_item_chip.setOnClickListener {
                    findNavController().navigate(
                        ProgressListFragmentDirections.actionProgressToGame(progressItem.boardId)
                    )
                }
            }
        }
    }
}
