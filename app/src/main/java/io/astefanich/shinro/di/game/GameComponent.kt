package io.astefanich.shinro.di.game

import dagger.Component
import dagger.Subcomponent
import io.astefanich.shinro.di.GameFragmentScope
import io.astefanich.shinro.di.ViewModelModule
import io.astefanich.shinro.ui.GameFragment

@GameFragmentScope
@Subcomponent(modules=[GameModule::class])
interface GameComponent {

    fun getBoardId(): Int

    fun inject(gameFragment: GameFragment)
}