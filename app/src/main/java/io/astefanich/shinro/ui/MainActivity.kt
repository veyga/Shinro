package io.astefanich.shinro.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import dagger.android.support.DaggerAppCompatActivity
import io.astefanich.shinro.R
import io.astefanich.shinro.ShinroApplication
import io.astefanich.shinro.di.activities.main.MainActivityComponent
import timber.log.Timber
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {


    lateinit var activityComponent: MainActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.MODE_NIGHT_YES

        val appComponent = (application as ShinroApplication).appComponent
        activityComponent = appComponent.getMainActivityComponentBuilder()
            .actitivtyContext(this)
            .build()
    }

   fun getMainActivityComponent(): MainActivityComponent {
       Timber.i("getting main activity component")
       return activityComponent
   }


}