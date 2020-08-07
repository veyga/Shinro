package io.astefanich.shinro.di.tips

import dagger.Component
import dagger.android.AndroidInjector
import io.astefanich.shinro.di.InstructionsFragmentScope
import io.astefanich.shinro.domain.Tip
import io.astefanich.shinro.ui.TipsFragment

@InstructionsFragmentScope
@Component(modules = [TipsModule::class])
interface TipsComponent : AndroidInjector<TipsFragment> {

    fun getInstructions(): List<Tip>

}