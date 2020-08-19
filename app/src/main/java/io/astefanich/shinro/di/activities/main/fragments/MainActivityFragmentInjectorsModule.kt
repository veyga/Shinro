package io.astefanich.shinro.di.activities.main.fragments

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.activities.main.fragments.about.AboutModule
import io.astefanich.shinro.ui.AboutFragment
import io.astefanich.shinro.ui.TitleFragment

//@PerFragment
@Module
abstract class MainActivityFragmentInjectorsModule {

    @ContributesAndroidInjector
    abstract fun providesTitleFragment(): TitleFragment

    @PerFragment //scope goes here, not above @Module
    @ContributesAndroidInjector(modules = [AboutModule::class])
    abstract fun providesAboutFragment(): AboutFragment

}