package io.astefanich.shinro.di.activities.main

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import dagger.android.AndroidInjector
import io.astefanich.shinro.di.activities.main.fragments.MainActivityFragmentInjectorsModule
import io.astefanich.shinro.di.PerActivity
import io.astefanich.shinro.di.activities.main.fragments.game.GameComponent
import io.astefanich.shinro.ui.MainActivity
import javax.inject.Named

//@PerActivity
//@Subcomponent(modules = [
//    MainActivityModule::class,
//    MainActivityFragmentInjectorsModule::class
//])
//interface MainActivityComponent : AndroidInjector<MainActivity> {
//
//    abstract fun getGameComponentBuilder(): GameComponent.Builder
//
//    @Subcomponent.Builder
//    interface Builder {
//
//        @BindsInstance
//        fun actitivtyContext(@Named("actCtx") context: Context): Builder
//
//        fun build(): MainActivityComponent
//    }
//}