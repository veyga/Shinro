package io.astefanich.shinro.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.ui.AboutFragment
import io.astefanich.shinro.ui.GameFragment
import io.astefanich.shinro.ui.InstructionsListFragment
import io.astefanich.shinro.ui.TitleFragment

@Module
abstract class FragmentInjectorsModule {

    @ContributesAndroidInjector
    abstract fun providesTitleFragment(): TitleFragment

    @ContributesAndroidInjector
    abstract fun providesGameFragment(): GameFragment

    @ContributesAndroidInjector
    abstract fun providesInstructionsFragment(): InstructionsListFragment

    @ContributesAndroidInjector
    abstract fun providesAboutFragment(): AboutFragment
}