package io.astefanich.shinro.viewmodels

import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Instruction
import timber.log.Timber
import javax.inject.Inject

class InstructionsViewModel @Inject constructor(val instructions: List<Instruction>) : ViewModel() {

    init{
        Timber.i("instructions viewmodel created")
        val instruction = instructions[1]
        Timber.i("instructions is null? ${instructions == null}")
        Timber.i("${instructions[1]}")
    }
}