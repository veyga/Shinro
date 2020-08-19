package io.astefanich.shinro.di.activities.main.fragments

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.activities.main.fragments.about.AboutModule
import io.astefanich.shinro.ui.AboutFragment
import io.astefanich.shinro.ui.TitleFragment

@Module
abstract class MainActivityFragmentInjectorsModule {

    @ContributesAndroidInjector
    abstract fun providesTitleFragment(): TitleFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [AboutModule::class])
    abstract fun providesAboutFragment(): AboutFragment

}