package io.astefanich.shinro.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import io.astefanich.shinro.viewmodels.ViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}