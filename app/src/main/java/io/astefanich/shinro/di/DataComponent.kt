package io.astefanich.shinro.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.Instruction

//@Component(
//    modules = [InstructionsModule::class]
//)
interface DataComponent {

    fun provideInstructions(): List<Instruction>

    fun provideBoards(): List<Board>
}