//package io.astefanich.shinro.di
//
//import androidx.arch.core.util.Function
//import dagger.Binds
//import dagger.Component
//import dagger.Module
//import dagger.Provides
//import timber.log.Timber
//
////class G constructor(val fn: Function1<Int, Boolean>)
//class FnWrapper constructor(val fn: (Int) -> Boolean)
//
//@Module
//class ModuleB {
//
//    @Provides
//    fun intToBoolean(): (Int) -> Boolean {
//        return { it == 2 }
//    }
//
//    @JvmSuppressWildcards
//    @Provides
//    fun g(intToBoolean: (Int) -> Boolean): FnWrapper = FnWrapper(intToBoolean)
//}
//
//@Component(modules = [ModuleB::class])
//interface ComponentB {
//    fun g(): FnWrapper
//}
//
//
//
