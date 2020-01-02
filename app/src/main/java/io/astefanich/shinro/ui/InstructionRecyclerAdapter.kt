package io.astefanich.shinro.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.astefanich.shinro.R
import io.astefanich.shinro.domain.Instruction
import io.astefanich.shinro.domain.InstructionType
import kotlinx.android.synthetic.main.instruction_list_item.view.*

class InstructionRecyclerAdapter(type: InstructionType) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //    @Inject
    lateinit var items: List<Instruction>

    init {
        items = when (type) {
            InstructionType.PATHFINDER -> pathfinderInstructions()
            InstructionType.BLOCKER -> blockerInstructions()
            InstructionType.PIGEONHOLE -> pigeonholeInstructions()
            else -> generalInstructions()
        }
    }

    fun generalInstructions(): List<Instruction> = arrayListOf(
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_01, 1, "generalstep1"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_02, 2, "generalstep2"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_03, 3, "generalstep3"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_04, 4, "generalstep4"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_05, 5, "generalstep5"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_06, 6, "generalstep6"),
        Instruction(InstructionType.GENERAL, R.drawable.ic_general_07, 7, "generalstep7")

    )

    fun pathfinderInstructions(): List<Instruction> = arrayListOf(
        Instruction(InstructionType.PATHFINDER, R.drawable.ic_general_01, 1, "pathfinderstep1"),
        Instruction(InstructionType.PATHFINDER, R.drawable.ic_general_02, 2, "pathfinderstep2"),
        Instruction(InstructionType.PATHFINDER, R.drawable.ic_general_03, 3, "pathfinderstep3"),
        Instruction(InstructionType.PATHFINDER, R.drawable.ic_general_04, 4, "pathfinderstep4"),
        Instruction(InstructionType.PATHFINDER, R.drawable.ic_general_05, 5, "pathfinderstep5"),
        Instruction(InstructionType.PATHFINDER, R.drawable.ic_general_06, 6, "pathfinderstep6"),
        Instruction(InstructionType.PATHFINDER, R.drawable.ic_general_07, 7, "pathfinderstep7")

    )

    fun blockerInstructions(): List<Instruction> = arrayListOf(
        Instruction(InstructionType.BLOCKER, R.drawable.ic_general_01, 1, "blockerstep1"),
        Instruction(InstructionType.BLOCKER, R.drawable.ic_general_02, 2, "blockerstep2"),
        Instruction(InstructionType.BLOCKER, R.drawable.ic_general_03, 3, "blockerstep3"),
        Instruction(InstructionType.BLOCKER, R.drawable.ic_general_04, 4, "blockerstep4"),
        Instruction(InstructionType.BLOCKER, R.drawable.ic_general_05, 5, "blockerstep5"),
        Instruction(InstructionType.BLOCKER, R.drawable.ic_general_06, 6, "blockerstep6"),
        Instruction(InstructionType.BLOCKER, R.drawable.ic_general_07, 7, "blockerstep7")

    )

    fun pigeonholeInstructions(): List<Instruction> = arrayListOf(
        Instruction(InstructionType.PIGEONHOLE, R.drawable.ic_general_01, 1, "pigeonholestep1"),
        Instruction(InstructionType.PIGEONHOLE, R.drawable.ic_general_02, 2, "pigeonholestep2"),
        Instruction(InstructionType.PIGEONHOLE, R.drawable.ic_general_03, 3, "piegonholestep3"),
        Instruction(InstructionType.PIGEONHOLE, R.drawable.ic_general_04, 4, "pigeonholestep4"),
        Instruction(InstructionType.PIGEONHOLE, R.drawable.ic_general_05, 5, "pigeonholestep5"),
        Instruction(InstructionType.PIGEONHOLE, R.drawable.ic_general_06, 6, "pigeonholestep6"),
        Instruction(InstructionType.PIGEONHOLE, R.drawable.ic_general_07, 7, "pigeonholestep7")

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