package io.astefanich.shinro.di.game

import dagger.BindsInstance
import dagger.Subcomponent
import dagger.android.AndroidInjector
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.domain.PlayRequest
import io.astefanich.shinro.ui.GameFragment

/*
 * Sub-component of AppComponent so it can access the repository and the ViewModelModule
 */
@PerFragment
@Subcomponent(modules = [GameModule::class, GameViewModelModule::class])
interface GameComponent : AndroidInjector<GameFragment> {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun playRequest(req: PlayRequest): Builder

        fun build(): GameComponent
    }
}