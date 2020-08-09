package io.astefanich.shinro.di.game

import dagger.BindsInstance
import dagger.Subcomponent
import dagger.android.AndroidInjector
import io.astefanich.shinro.di.PerApplication
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.ui.GameFragment
import javax.inject.Named

/*
 * Subcomponent of AppComponent so it can access the repository and the ViewModelModule
 */
//@PerFragment
//@Subcomponent(modules = [GameModule::class])
//interface GameComponent : AndroidInjector<GameFragment> {
//
//    @Subcomponent.Builder
//    interface Builder {
//
//        @BindsInstance
//        fun boardId(id: Int): Builder
//
//        fun build(): GameComponent
//    }
//}