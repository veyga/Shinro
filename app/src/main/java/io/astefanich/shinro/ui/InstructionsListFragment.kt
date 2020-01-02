package io.astefanich.shinro.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.InstructionsListFragmentBinding

/**
 * A simple [Fragment] subclass.
 */
class InstructionsListFragment : Fragment() {

    lateinit var instructionType: String

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

        val recyclerAdapter = InstructionRecyclerAdapter(instructionsListArgs.instructionType)
        binding.instructionsRecyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

}
