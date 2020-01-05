package io.astefanich.shinro.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.ui.AboutFragment
import io.astefanich.shinro.ui.TitleFragment

@Module
abstract class FragmentInjectorsModule {

    @ContributesAndroidInjector
    abstract fun providesTitleFragment(): TitleFragment

    @ContributesAndroidInjector
    abstract fun providesAboutFragment(): AboutFragment

}