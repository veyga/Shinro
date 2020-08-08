package io.astefanich.shinro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.ProgressFragmentBinding
import io.astefanich.shinro.domain.ProgressItem
import io.astefanich.shinro.util.ProgressRecyclerAdapter
import javax.inject.Inject

class ProgressFragment : Fragment() {

    @Inject
    lateinit var items: List<ProgressItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        val binding: ProgressFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.progress_fragment, container, false
        )
        val recyclerAdapter = ProgressRecyclerAdapter(items)
        binding.progressRecyclerView.apply {
            adapter = recyclerAdapter
        }
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
//        return inflater.inflate(R.layout.progress_fragment, container, false)
    }
}

