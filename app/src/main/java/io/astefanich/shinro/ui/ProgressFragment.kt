package io.astefanich.shinro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.domain.ProgressItem
import timber.log.Timber
import javax.inject.Inject

class ProgressFragment : Fragment() {

    @Inject
    lateinit var items: List<ProgressItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val binding: ProgressFragmentBinding = DataBindingUtil.inflate(
//            inflater, R.layout.progress_fragment, container, false
//        )

        AndroidSupportInjection.inject(this)
        Timber.i("progress fragment created\n$items")
        return inflater.inflate(R.layout.progress_fragment, container, false)
    }
}

