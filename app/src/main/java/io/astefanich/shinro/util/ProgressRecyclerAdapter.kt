package io.astefanich.shinro.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.astefanich.shinro.R
import io.astefanich.shinro.domain.ProgressItem
import io.astefanich.shinro.ui.ProgressFragmentDirections
import kotlinx.android.synthetic.main.progess_item.view.*

class ProgressRecyclerAdapter(private val items: List<ProgressItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProgressItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.progess_item,
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

        fun bind(progressItem: ProgressItem) {
            itemView.apply {
                progress_item_chip.setText("Puzzle ${progressItem.boardId}\t(${progressItem.difficulty})      ")
                val icon = if (progressItem.completed) R.drawable.checkmark else R.drawable.delete
                progress_item_chip.setChipIconResource(icon)
                progress_item_chip.setOnClickListener {
                    findNavController().navigate(
                        ProgressFragmentDirections.actionProgressToGame(progressItem.boardId)
                    )
                }
            }
        }
    }
}
