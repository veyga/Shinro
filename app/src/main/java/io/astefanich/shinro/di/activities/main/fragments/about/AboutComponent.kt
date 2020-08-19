package io.astefanich.shinro.di.activities.main.fragments.about

import dagger.Subcomponent
import io.astefanich.shinro.di.PerFragment

@PerFragment
@Subcomponent(modules = [AboutModule::class])
interface AboutComponent {

}

