package io.astefanich.shinro.di.instructions

import dagger.Component
import io.astefanich.shinro.di.InstructionsFragmentScope
import io.astefanich.shinro.domain.Instruction
import io.astefanich.shinro.ui.InstructionsListFragment

@InstructionsFragmentScope
@Component(modules = [InstructionsModule::class])
interface InstructionsComponent {

    fun getInstructions(): List<Instruction>

    fun inject(fragment: InstructionsListFragment)

}