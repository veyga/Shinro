package io.astefanich.shinro.viewmodels

import androidx.lifecycle.ViewModel
import com.radutopor.viewmodelfactory.annotations.Provided
import com.radutopor.viewmodelfactory.annotations.ViewModelFactory
import io.astefanich.shinro.domain.Instruction
import javax.inject.Inject

@ViewModelFactory
class InstructionsViewModel @Inject constructor(@Provided val instructions: List<Instruction>): ViewModel(){

}