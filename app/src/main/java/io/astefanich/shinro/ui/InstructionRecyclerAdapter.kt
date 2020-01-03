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
    var items: List<Instruction>

    init {
        items = when (type) {
            InstructionType.PATHFINDER -> pathfinderInstructions()
            InstructionType.BLOCKER -> blockerInstructions()
            InstructionType.PIGEONHOLE -> pigeonholeInstructions()
            else -> generalInstructions()
        }
    }

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

        fun bind(instruction: Instruction) {
            itemView.apply {
                instruction_image.setImageResource(instruction.drawable)
                instruction_text.setText(instruction.text)
            }
        }
    }

    fun generalInstructions(): List<Instruction> = arrayListOf(
        Instruction(
            R.drawable.ic_general_01, """
           A Shinro puzzle is an 8x8 grid. 
           Your job is to locate twelve 
           hidden marbles, based on clues 
           in the puzzle (numbers and 
           arrows). 
           The numbers point to how
           many marbles are found 
           in each column/row. 
           Each arrow points to atleast 
           one marble. Not every marble 
           has an arrow pointing to it.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_02, """
            Every puzzle can be solved 
            entirely with logic and 
            reason. 
            No guessing required!
            If you get stuck, 
            watch the instructional 
            video and read the 
            advanced tips.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_03, """
            This column has zero marbles, 
            so we can mark off each 
            of its empty squares.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_04, """
            Similarly, these rows 
            and column also have 
            zero marbles, so we 
            can eliminate the 
            empty squares.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_05, """
            Each of these arrows point to
            a single empty square. 
            Therefore, that square 
            must contain a marble.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_06, """
            This column is now satisfied, 
            so we can eliminate 
            the remaining empty squares.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_07, """
            The arrow here is pointing 
            to the left. 
            There is only one marble 
            left to find in this row. 
            It cannot be found in the 
            empty squares to the
            arrow's right, because 
            then satisfying
            the arrow becomes impossible.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_08, """
            This arrow points to a 
            single empty square. 
            Therefore, it must
            contain a marble.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_09, """
            This row is now satisfied.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_10, """
            This arrow points to a 
            single empty square. 
            Thus, the empty
            square must contain 
            a marble.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_11, """
            The eliminated square here 
            cannot contain a marble. 
            Otherwise, 
            the left-facing arrow 
            cannot be satisfied.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_12, """
            This column has three marbles,
            and three empty squares. 
            Therefore, the empty squares 
            must contain marbles.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_13, """
            This row is now satisfied.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_14, """
            There are two marbles in 
            this column, so its last 
            remaining empty square 
            must contain a marble.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_15, """
            The arrow here points 
            diagonally to only one 
            empty square. 
            Therefore, the empty 
            square must contain 
            a marble.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_16, """
            This row and column are now
            satisfied, so we can 
            eliminate all of 
            their remaining empty 
            squares.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_17, """
            We found two of the three
            marbles in this row, 
            and there is only empty 
            square remaining.
            Therefore, the square must 
            contain the third marble.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_18, """
            This column is now satisfied.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_general_19, """
            This column has two marbles, 
            and only two empty squares. 
            Therefore, the empty squares 
            must contain marbles.
            You've solved the puzzle!
        """.trimIndent()
        )

    )

    fun pathfinderInstructions(): List<Instruction> = arrayListOf(
        Instruction(R.drawable.ic_general_01, "pathfinderstep1"),
        Instruction(R.drawable.ic_general_02, "pathfinderstep2"),
        Instruction(R.drawable.ic_general_03, "pathfinderstep3"),
        Instruction(R.drawable.ic_general_04, "pathfinderstep4"),
        Instruction(R.drawable.ic_general_05, "pathfinderstep5"),
        Instruction(R.drawable.ic_general_06, "pathfinderstep6"),
        Instruction(R.drawable.ic_general_07, "pathfinderstep7")

    )

    fun blockerInstructions(): List<Instruction> = arrayListOf(
        Instruction(R.drawable.ic_general_01, "blockerstep1"),
        Instruction(R.drawable.ic_general_02, "blockerstep2"),
        Instruction(R.drawable.ic_general_03, "blockerstep3"),
        Instruction(R.drawable.ic_general_04, "blockerstep4"),
        Instruction(R.drawable.ic_general_05, "blockerstep5"),
        Instruction(R.drawable.ic_general_06, "blockerstep6"),
        Instruction(R.drawable.ic_general_07, "blockerstep7")

    )

    fun pigeonholeInstructions(): List<Instruction> = arrayListOf(
        Instruction(R.drawable.ic_general_01, "pigeonholestep1"),
        Instruction(R.drawable.ic_general_02, "pigeonholestep2"),
        Instruction(R.drawable.ic_general_03, "piegonholestep3"),
        Instruction(R.drawable.ic_general_04, "pigeonholestep4"),
        Instruction(R.drawable.ic_general_05, "pigeonholestep5"),
        Instruction(R.drawable.ic_general_06, "pigeonholestep6"),
        Instruction(R.drawable.ic_general_07, "pigeonholestep7")

    )

}