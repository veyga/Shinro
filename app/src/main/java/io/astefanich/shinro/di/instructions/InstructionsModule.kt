package io.astefanich.shinro.di.instructions

import dagger.Module
import dagger.Provides
import io.astefanich.shinro.R
import io.astefanich.shinro.di.InstructionsFragmentScope
import io.astefanich.shinro.domain.Instruction
import io.astefanich.shinro.domain.InstructionType

/*
* Hard coding instructions since they won't change.
 */
@Module
class InstructionsModule(private val instructionType: InstructionType) {

    @InstructionsFragmentScope
    @Provides
    fun provideInstructionType() = instructionType

    @InstructionsFragmentScope
    @Provides
    fun provideInstructions(type: InstructionType): List<Instruction> = when (type) {
        InstructionType.PATHFINDER -> pathfinderInstructions()
        InstructionType.BLOCKER -> blockerInstructions()
        InstructionType.PIGEONHOLE -> pigeonholeInstructions()
        else -> generalInstructions()
    }

    private fun generalInstructions(): List<Instruction> = arrayListOf(
        Instruction(
            R.mipmap.ic_general_01_foreground, """
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
            R.mipmap.ic_general_02_foreground, """
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
            R.mipmap.ic_general_03_foreground, """
            This column has zero marbles, 
            so we can mark off each 
            of its empty squares.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_04_foreground, """
            Similarly, these rows 
            and column also have 
            zero marbles, so we 
            can eliminate the 
            empty squares.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_05_foreground, """
            Each of these arrows point to
            a single empty square. 
            Therefore, that square 
            must contain a marble.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_06_foreground, """
            This column is now satisfied, 
            so we can eliminate 
            the remaining empty squares.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_07_foreground, """
            The arrow here is pointing 
            to the left. 
            There is only one marble 
            left to find in this row. 
            It cannot be found in the 
            empty squares to the
            arrow's right, because 
            then satisfying
            the arrow becomes 
            impossible.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_08_foreground, """
            This arrow points to a 
            single empty square. 
            Therefore, it must
            contain a marble.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_09_foreground, """
            This row is now satisfied.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_10_foreground, """
            This arrow points to a 
            single empty square. 
            Thus, the empty
            square must contain 
            a marble.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_11_foreground, """
            The eliminated square here 
            cannot contain a marble. 
            Otherwise, 
            the left-facing arrow 
            cannot be satisfied.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_12_foreground, """
            This column has three marbles,
            and three empty squares. 
            Therefore, the empty squares 
            must contain marbles.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_13_foreground, """
            This row is now satisfied.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_14_foreground, """
            There are two marbles in 
            this column, so its last 
            remaining empty square 
            must contain a marble.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_15_foreground, """
            The arrow here points 
            diagonally to only one 
            empty square. 
            Therefore, the empty 
            square must contain 
            a marble.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_16_foreground, """
            This row and column are now
            satisfied, so we can 
            eliminate all of 
            their remaining empty 
            squares.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_17_foreground, """
            We found two of the three
            marbles in this row, 
            and there is only empty 
            square remaining.
            Therefore, the square must 
            contain the third marble.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_18_foreground, """
            This column is now satisfied.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_general_19_foreground, """
            This column has two marbles, 
            and only two empty squares. 
            Therefore, the empty squares 
            must contain marbles.
            You've solved the puzzle!
        """.trimIndent()
        )

    )

    private fun pathfinderInstructions(): List<Instruction> = arrayListOf(
        Instruction(
            R.mipmap.ic_pathfinder_01_foreground, """
            All of the simple moves
            have already been taken in
            this puzzle. Where can
            we find another move?
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_pathfinder_02_foreground, """
            This arrow is satisfied
            if there is at least one
            marble along path [A].
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_pathfinder_03_foreground, """
            This arrow is satisfied
            if there is at least one
            marble found along path
            marked [B].
            The [A] and [B] paths do
            not overlap. Thus, we
            know there must be
            at least two marbles found
            along these paths.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_pathfinder_04_foreground, """
            These rows completely
            contain the two paths. No
            squares in either path fall
            outside of the area covered
            by these rows.
            There are two total hidden
            marbles in these rows, based
            on row counts. And we know,
            our two paths contain two
            marbles. Thus, the other empty
            squares in these rows cannot
            possibly contain marbles,
            and can be eliminated.
        """.trimIndent()
        )
    )

    private fun blockerInstructions(): List<Instruction> = arrayListOf(
        Instruction(
            R.mipmap.ic_blocker_01_foreground, """
            Here is another puzzle with
            all of the simple moves
            already taken. Can you
            find the next move?
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_blocker_02_foreground, """
            This arrow is satisfied
            by a marble in [A] or [B].
            If we tried to place a
            marble at [C], then
            it would be impossible
            to satisfy the arrow.

            - We cannot put a marble at [B],
            because then its row's marble
            count would be violated.
            - We cannot put a marble at [A],
            because then its column's
            marble count could be violated.

            So, [C] is a blocker.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_blocker_03_foreground, """
           Thus, we can eliminate [C]
            as a possibility.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_blocker_04_foreground, """
            Here's another example using
            the same puzzle.
            This arrow is satisfied by
            a marble in [A] or [B].
            If we place a marble at [C],
            then we just made it
            impossible to satisfy
            the arrow.

            - We cannot put a marble
            at [B], because then its
            column's marble count
            would be violated.
            - We cannot put a marble
            at [A], because then its
            column's marble count
            would be violated.

            So, [C] is a blocker.
        """.trimIndent()
        ),
        Instruction(
            R.mipmap.ic_blocker_05_foreground, """
            Thus, we can eliminate [C]
            as a possibility.
        """.trimIndent()
        )

    )

    private fun pigeonholeInstructions(): List<Instruction> = arrayListOf(
        Instruction(
            R.drawable.ic_pigeonhole_1, """
            This is the most advanced
            technique required by
            some of the hardest puzzles.
            Finding these moves can
            be very difficult.

            Here is a puzzle with no
            more obvious moves available.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_pigeonhole_2, """
            This arrow is satisfied by
            a marble in [a] or [b].
            The row with squares [c],[d],[e],
            and [f] has three hidden marbles
            but four available squares to
            select from. If we put a marble
            in [a], then its column is
            satisfied, [e] is eliminated,
            and we then know there are
            marbles in [c],[d], and [f].
            But if we put a marble in [b],
            then its column is satisfied,
            [f] is eliminated, and then
            we know there are marbles in
            [c], [d], and [e].
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_pigeonhole_3, """
            Therefore, there are only
            possibilities.

            - Marbles in [c],[d],[e]
            - Marbles in [c],[d],[f]

            Both possibilities have
            marbles in [c] and [d],
            so we know these squares
            must contain marbles.
            We can now mark them
            with certainty.
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_pigeonhole_4, """
            Another example:
            This arrow is satisfed by
            a marble in [a] or [b]. The
            column with squares [c], [d],
            [e], and [f] has three hidden
            marbles but four available
            squares to select from.
            If we put a marble in [a],
            then its row is satisfied,
            [d] is eliminated, and then
            we know there are marbles in
            [c], [e], and [f].
            But if we put a marble in [b],
            then its row is satisfied,
            [f] is eliminated and then we
            know there are marbles in
            [c], [d], and [e].
        """.trimIndent()
        ),
        Instruction(
            R.drawable.ic_pigeonhole_5, """
            Therefore, there are
            only two possibilities.

            - Marbles in [c], [e], [f]
            - Marbles in [c], [d], [e]

            Both possibilities have marbles
            in [c] and [e], so we know
            that these squares must
            contain marbles.
            We can now mark them
            with certainty.
        """.trimIndent()
        )

    )
}
