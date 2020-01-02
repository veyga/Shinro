package io.astefanich.shinro.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.astefanich.shinro.R
import io.astefanich.shinro.domain.Instruction
import io.astefanich.shinro.domain.InstructionType
import kotlinx.android.synthetic.main.instruction_list_item.view.*

class InstructionRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Instruction> = arrayListOf(
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_01, 1, "step1"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_02, 2, "step2"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_03, 3, "step3"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_04, 4, "step4"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_05, 5, "step5"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_06, 6, "step6"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_07, 7, "step7")

    )

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

    class InstructionViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val instructionImage = itemView.instruction_image
        val instructionText = itemView.instruction_text

        fun bind(instruction: Instruction) {
            instructionText.setText(instruction.text)
            instructionImage.setImageResource(instruction.drawable)
        }
    }
}