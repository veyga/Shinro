package io.astefanich.shinro.di.activities.main.fragments

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.app.ViewModelKey
import io.astefanich.shinro.ui.StatisticsFragment
import io.astefanich.shinro.viewmodels.StatisticsViewModel

@PerFragment
@Subcomponent(modules = [StatisticsViewModelModule::class])
interface StatisticsComponent {

    fun inject(frag: StatisticsFragment)
}

@Module
abstract class StatisticsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StatisticsViewModel::class)
    abstract fun bindStatisticsViewModel(vm: StatisticsViewModel): ViewModel
}

@Module
class StatisticsModule {

}

