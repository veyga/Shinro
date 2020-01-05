package io.astefanich.shinro.di

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class GameFragmentScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class InstructionsFragmentScope
