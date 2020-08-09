package io.astefanich.shinro.di.about

import dagger.Subcomponent
import io.astefanich.shinro.di.PerFragment

@PerFragment
@Subcomponent(modules = [AboutModule::class])
interface AboutComponent {

}

