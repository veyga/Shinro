package io.astefanich.shinro.di.activities.main.fragments


import android.net.Uri
import dagger.Component
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.BuildConfig
import io.astefanich.shinro.R
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.ui.AboutFragment

@PerFragment
@Component(modules = [AboutModule::class])
interface AboutComponent {

    fun inject(frag: AboutFragment)

}

@Module
object AboutModule {

    @PerFragment
    @Provides
    @JvmStatic
    fun providesVersionName(): String = BuildConfig.VERSION_NAME

    @PerFragment
    @Provides
    @JvmStatic
    internal fun providesVideoURI(): Uri =
        Uri.parse("android.resource://io.astefanich.shinro/" + R.raw.what_is_shinro)
}
