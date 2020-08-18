package io.astefanich.shinro.di.tips

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import io.astefanich.shinro.common.TipChoice
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.ui.TipsDetailListFragment

@PerFragment
@Component(modules = [TipsModule::class])
interface TipsComponent : AndroidInjector<TipsDetailListFragment> {

//This is only needed if you want other scopes to be able to access List<Tip> provided by the module
//    fun getTips(): List<Tip>

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun tipChoice(choice: TipChoice): Builder

        fun build(): TipsComponent
    }
}