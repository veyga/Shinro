package io.astefanich.shinro.di.tips

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import io.astefanich.shinro.di.PerInstructionsFragment
import io.astefanich.shinro.domain.TipChoice
import io.astefanich.shinro.ui.TipsDetailListFragment

@PerInstructionsFragment
@Component(modules = [TipsModule::class])
interface TipsComponent : AndroidInjector<TipsDetailListFragment> {

//This is only needed if you want other scopes to be able to access List<Tip> provided by the module
//    fun getTips(): List<Tip>

    @Component.Builder
    interface Builder {

        //allows dagger to auto create/inject the module; it can access the choice
        @BindsInstance
        fun tipChoice(choice: TipChoice): Builder

        fun build(): TipsComponent
    }
}