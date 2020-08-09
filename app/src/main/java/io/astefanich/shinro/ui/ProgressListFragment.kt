package io.astefanich.shinro.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.FragmentProgressListBinding
import io.astefanich.shinro.domain.Progress
import io.astefanich.shinro.util.ProgressRecyclerAdapter
import javax.inject.Inject

class ProgressListFragment : Fragment() {

    @Inject
    lateinit var items: List<Progress>

    @Inject
    lateinit var mContext: Context

    private lateinit var binding: FragmentProgressListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_progress_list, container, false
        )
        val recyclerAdapter = ProgressRecyclerAdapter(items)
        binding.progressRecyclerView.apply {
            adapter = recyclerAdapter
        }

//        ArrayAdapter.createFromResource(
//            mContext,
//            R.array.completion_statuses,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.completionFilterSpinner.adapter = adapter
//        }

//        ArrayAdapter.createFromResource(
//            mContext,
//            R.array.difficulties,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.difficultiesFilterSpinner.adapter = adapter
//        }

        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
//        return inflater.inflate(R.layout.fragment_progress_list, container, false)
    }

    //    the spinners don't disappear at same time as fragment (bleed into next fragment transition)
    override fun onStop() {
        super.onStop()
        binding.completionFilterSpinner.visibility = View.INVISIBLE
        binding.difficultiesFilterSpinner.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        binding.completionFilterSpinner.visibility = View.VISIBLE
        binding.difficultiesFilterSpinner.visibility = View.VISIBLE
    }

    //TODO implement this after implementing LiveData
//    val filterListener = object : AdapterView.OnItemSelectedListener {
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//        }
//
//        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            Timber.i("filtering")
//        }
//    }
}
//private enum class FilterType { COMPLETION_STATUS, DIFFICULTY }

