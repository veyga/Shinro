package io.astefanich.shinro.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.TipsFragmentBinding
import io.astefanich.shinro.di.tips.DaggerTipsComponent
import io.astefanich.shinro.di.tips.TipsComponent
import io.astefanich.shinro.di.tips.TipsModule
import io.astefanich.shinro.domain.Tip
import io.astefanich.shinro.domain.TipChoice
import io.astefanich.shinro.util.TipsRecyclerAdapter
import javax.inject.Inject

class TipsFragment : Fragment() {

    lateinit var tipChoice: TipChoice
    lateinit var component: TipsComponent

    @Inject
    lateinit var items: List<Tip>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: TipsFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.tips_fragment, container, false
        )

        val tipChoiceArg by navArgs<TipsFragmentArgs>()
        tipChoice = tipChoiceArg.tipChoice

        component = DaggerTipsComponent
            .builder()
            .tipsModule(TipsModule(tipChoice))
            .build()
        component.inject(this)

        val recyclerAdapter = TipsRecyclerAdapter(items)
        binding.tipsRecyclerView.apply {
            adapter = recyclerAdapter
        }
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

}
