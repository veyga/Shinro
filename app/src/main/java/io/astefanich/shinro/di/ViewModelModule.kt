package io.astefanich.shinro.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.*
import dagger.multibindings.IntoMap
import io.astefanich.shinro.viewmodels.GameViewModel
import io.astefanich.shinro.viewmodels.ProgressViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import java.lang.annotation.Documented
import javax.inject.Provider
import kotlin.reflect.KClass

//@Target(
//    AnnotationTarget.FUNCTION,
//    AnnotationTarget.PROPERTY_GETTER,
//    AnnotationTarget.PROPERTY_SETTER
//)
@Documented
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    //    @PerApplication
//    @Reusable
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    abstract fun bindGameViewModel(gameViewModel: GameViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(ProgressViewModel::class)
//    abstract fun bindProgressViewModel(progresViewModel: ProgressViewModel): ViewModel

}

//@Module
//class ViewModelFactoryModule {
//
//    @PerApplication
//    @Provides
//    fun providesViewModelFactory(
//        providerMap: Map<Class<out ViewModel>, Provider<ViewModel>>
//    ): ViewModelProvider.Factory {
//        return ViewModelFactory(providerMap)
//    }
//}
//

