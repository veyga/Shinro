package io.astefanich.shinro.di.game

import dagger.Subcomponent
import dagger.android.AndroidInjector
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.ui.GameFragment

/*
 * Subcomponent of AppComponent so it can access the repository and the ViewModelModule
 */
@PerFragment
@Subcomponent(modules = [GameModule::class])
interface GameComponent : AndroidInjector<GameFragment> {

    fun getBoardId(): Int
//
//    fun getWinBuzzPattern(): LongArray
//
//    fun getResetBuzzPattern(): LongArray

    @Subcomponent.Builder
    interface Builder {

        fun gameModule(module: GameModule): Builder
//        fun boardId(id: Int): Builder

        fun build(): GameComponent
    }
}