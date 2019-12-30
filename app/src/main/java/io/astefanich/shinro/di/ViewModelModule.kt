package io.astefanich.shinro.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import io.astefanich.shinro.viewmodels.GameViewModel
import io.astefanich.shinro.viewmodels.InstructionsViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import kotlin.reflect.KClass

@Module
internal abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    abstract fun bindGameViewModel(viewModel: GameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InstructionsViewModel::class)
    abstract fun bindInstructionViewModel(viewModel: InstructionsViewModel): ViewModel
}


@Documented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
