package io.astefanich.shinro.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import io.astefanich.shinro.ui.game.GameViewModel
import io.astefanich.shinro.ui.game.TitleViewModel
import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    abstract fun gameTitleViewModel(viewModel: GameViewModel): ViewModel
}


//@Documented
//@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)
//@MapKey
//internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
//


// DAGGER 2.21
@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)