package io.astefanich.shinro.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.astefanich.shinro.R
import io.astefanich.shinro.domain.Instruction
import kotlinx.android.synthetic.main.instruction_list_item.view.*

class InstructionRecyclerAdapter(private val items: List<Instruction>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InstructionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.instruction_list_item,
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

        fun bind(instruction: Instruction) {
            itemView.apply {
                instruction_image.setImageResource(instruction.drawable)
                instruction_text.setText(instruction.text)
            }
        }
    }


}