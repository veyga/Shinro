package io.astefanich.shinro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import io.astefanich.shinro.R

class GameSummaryFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_summary, container, false)
        val gameOverFragmentArgs by navArgs<GameSummaryFragmentArgs>()
        var gameSummary = gameOverFragmentArgs.gameSummary

//        val gameComponent = (activity!!.application as ShinroApplication)
//            .appComponent
//            .getGameComponentBuilder()
//            .playRequest(playRequest)
//            .build()
//        gameComponent.inject(this)
//        (activity!!.application as ShinroApplication)
//            .appComponent
//            .getGameComponentBuilder()
//            .playRequest(playRequest)
//            .build()
//            .inject(this)

//        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        return layoutInflater.inflate(R.layout.fragment_game_summary, container, false)

    }
}