package io.astefanich.shinro.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R

/**
 * A simple [Fragment] subclass.
 */
class InstructionsListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)

        val instructionsListArgs by navArgs<InstructionsListFragmentArgs>()
        val instructionType = instructionsListArgs.instructionType

        val view = inflater.inflate(R.layout.instructions_list_fragment, container, false)
        val recyclerAdapter = InstructionRecyclerAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.instructions_recycler_view)
        recyclerView.apply{
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        return view
    }

}
