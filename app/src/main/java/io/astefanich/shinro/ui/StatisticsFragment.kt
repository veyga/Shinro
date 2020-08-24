package io.astefanich.shinro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)
        return binding.root
//        return super.onCreateView(inflater, container, savedInstanceState)
    }
}