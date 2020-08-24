package io.astefanich.shinro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.FragmentStatisticsBinding
import io.astefanich.shinro.viewmodels.StatisticsViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject

class StatisticsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: StatisticsViewModel

    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity)
            .mainActivityComponent
            .getStatisticsComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.i("got the factory: $viewModelFactory")
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(StatisticsViewModel::class.java)
        Timber.i("created the vm: $viewModel")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
//        viewModel.easyStats.observe(viewLifecycleOwner, {stat ->
//            binding.easyNumPlayed.text = stat.numPlayed
//        })
        return binding.root
//        return super.onCreateView(inflater, container, savedInstanceState)
    }
}