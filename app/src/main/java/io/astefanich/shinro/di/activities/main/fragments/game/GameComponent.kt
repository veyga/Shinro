package io.astefanich.shinro.di.activities.main.fragments.game

import dagger.BindsInstance
import dagger.Subcomponent
import dagger.android.AndroidInjector
import io.astefanich.shinro.common.PlayRequest
import io.astefanich.shinro.common.TimeSeconds
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.ui.GameFragment

///*
// * Sub-component of AppComponent so it can access the repository and the ViewModelModule
// */
//@PerFragment
//@Subcomponent(modules = [GameModule::class, GameViewModelModule::class])
//interface GameComponent : AndroidInjector<GameFragment> {
//
//    @Subcomponent.Builder
//    interface Builder {
//
//        @BindsInstance
//        fun playRequest(req: PlayRequest): Builder
//
////        @BindsInstance
////        fun timerIncrements(period: TimeSeconds): Builder
//
//        fun build(): GameComponent
//    }
//}