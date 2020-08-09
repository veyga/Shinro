package io.astefanich.shinro.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.di.about.AboutModule
import io.astefanich.shinro.ui.AboutFragment
import io.astefanich.shinro.ui.ProgressListFragment
import io.astefanich.shinro.ui.TitleFragment

@Module
abstract class FragmentInjectorsModule {

    @ContributesAndroidInjector
    abstract fun providesTitleFragment(): TitleFragment

    @ContributesAndroidInjector
    abstract fun providesProgressFragment(): ProgressListFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [AboutModule::class])
    abstract fun providesAboutFragment(): AboutFragment

}