package io.astefanich.shinro.di

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerApplication

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerFragment

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerInstructionsFragment
