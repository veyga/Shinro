package io.astefanich.shinro.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import io.astefanich.shinro.R
import io.astefanich.shinro.ShinroApplication
import io.astefanich.shinro.di.activities.main.MainActivityComponent


class MainActivity : AppCompatActivity() {

    val mainActivityComponent: MainActivityComponent by lazy {
        (application as ShinroApplication)
            .appComponent
            .getMainActivityComponentBuilder()
            .activityContext(this)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.MODE_NIGHT_YES
        mainActivityComponent.inject(this)
//        (application as ShinroApplication)
//            .appComponent
//            .getMainActivityComponentBuilder()
//            .actitivtyContext(this)
//            .build()
//        val appComponent = (application as ShinroApplication).appComponent
//        activityComponent = appComponent.getMainActivityComponentBuilder()
//            .actitivtyContext(this)
//            .build()
    }

//   fun getMainActivityComponent(): MainActivityComponent {
//       Timber.i("getting main activity component")
//       return activityComponent
//   }

}