package io.astefanich.shinro.di.game

import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import dagger.android.AndroidInjector
import io.astefanich.shinro.di.GameFragmentScope
import io.astefanich.shinro.di.ViewModelModule
import io.astefanich.shinro.ui.GameFragment

/*
 * Subcomponent of AppComponent so it can access the repository and the ViewModelModule
 */
@GameFragmentScope
@Subcomponent(modules = [GameModule::class])
interface GameComponent : AndroidInjector<GameFragment> {

    fun getBoardId(): Int

    @Subcomponent.Builder
    interface Builder {

        fun gameModule(module: GameModule): Builder

        fun build(): GameComponent
    }

//    @Subcomponent.Factory
//    interface Factory {
//        fun create(@BindsInstance boardId: Int): GameComponent
//    }
//
//    fun inject(fragment: GameFragment)
}