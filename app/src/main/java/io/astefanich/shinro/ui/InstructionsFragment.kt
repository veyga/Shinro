package io.astefanich.shinro.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection

import io.astefanich.shinro.R
import io.astefanich.shinro.viewmodels.GameViewModel
import io.astefanich.shinro.viewmodels.InstructionsViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class InstructionsFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: InstructionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)
        // Inflate the layout for this fragment
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(InstructionsViewModel::class.java)
        return inflater.inflate(R.layout.instructions_fragment, container, false)
    }


}
