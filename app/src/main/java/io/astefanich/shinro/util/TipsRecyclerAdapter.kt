package io.astefanich.shinro.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.astefanich.shinro.R
import io.astefanich.shinro.domain.Tip
import kotlinx.android.synthetic.main.tip_item.view.*

class TipsRecyclerAdapter(private val items: List<Tip>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InstructionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.tip_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is InstructionViewHolder -> {
                holder.bind(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int = items.size

    internal class InstructionViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(tip: Tip) {
            itemView.apply {
                tip_image.setImageResource(tip.drawable)
                tip_text.setText(tip.text)
            }
        }
    }


}