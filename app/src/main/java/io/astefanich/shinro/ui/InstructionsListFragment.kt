package io.astefanich.shinro.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.InstructionsListFragmentBinding
import io.astefanich.shinro.domain.Instruction
import io.astefanich.shinro.domain.InstructionType
import io.astefanich.shinro.util.InstructionRecyclerAdapter
import javax.inject.Inject

class InstructionsListFragment : Fragment() {

    lateinit var instructionType: InstructionType

    @Inject
    lateinit var items: List<Instruction>


    //    init {
//        items = when (type) {
//            InstructionType.PATHFINDER -> pathfinderInstructions()
//            InstructionType.BLOCKER -> blockerInstructions()
//            InstructionType.PIGEONHOLE -> pigeonholeInstructions()
//            else -> generalInstructions()
//        }
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)

        val binding: InstructionsListFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.instructions_list_fragment, container, false
        )

        val instructionsListArgs by navArgs<InstructionsListFragmentArgs>()
        instructionType = instructionsListArgs.instructionType
        val recyclerAdapter =
            InstructionRecyclerAdapter(items)
        binding.instructionsRecyclerView.apply {
            adapter = recyclerAdapter
        }
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

}
