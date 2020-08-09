package io.astefanich.shinro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.FragmentProgressListBinding
import io.astefanich.shinro.util.ProgressAdapter
import io.astefanich.shinro.viewmodels.ProgressViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import javax.inject.Inject

class ProgressListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentProgressListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_progress_list, container, false
        )
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProgressViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        val adapter = ProgressAdapter()
        binding.progressRecyclerView.adapter = adapter
        viewModel.progress.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

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
}

