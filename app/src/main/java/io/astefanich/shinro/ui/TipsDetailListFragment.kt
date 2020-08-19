package io.astefanich.shinro.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import io.astefanich.shinro.R
import io.astefanich.shinro.common.Tip
import io.astefanich.shinro.common.TipChoice
import io.astefanich.shinro.databinding.FragmentTipsDetailListBinding
import io.astefanich.shinro.di.activities.main.fragments.tips.DaggerTipsComponent
import io.astefanich.shinro.util.TipsRecyclerAdapter
import javax.inject.Inject

class TipsDetailListFragment : Fragment() {

    lateinit var tipChoice: TipChoice

    @Inject
    lateinit var items: List<Tip>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTipsDetailListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tips_detail_list, container, false
        )

        val tipChoiceArg by navArgs<TipsDetailListFragmentArgs>()
        tipChoice = tipChoiceArg.tipChoice

        val component = DaggerTipsComponent
            .builder()
            .tipChoice(tipChoice)
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
