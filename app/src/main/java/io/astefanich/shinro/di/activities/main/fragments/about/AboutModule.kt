package io.astefanich.shinro.di.activities.main.fragments.about

import android.net.Uri
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.R
import io.astefanich.shinro.di.PerFragment

@Module
object AboutModule {

    @PerFragment
    @Provides
    @JvmStatic
    internal fun providesVideoURI(): Uri =
        Uri.parse("android.resource://io.astefanich.shinro/" + R.raw.what_is_shinro)
}