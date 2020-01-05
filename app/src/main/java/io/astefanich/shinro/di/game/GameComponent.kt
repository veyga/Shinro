package io.astefanich.shinro.di.game

import dagger.Component
import io.astefanich.shinro.di.GameFragmentScope
import io.astefanich.shinro.ui.GameFragment

@GameFragmentScope
@Component
interface GameComponent {

    fun getBoardId(): Int

    fun inject(gameFragment: GameFragment)
}