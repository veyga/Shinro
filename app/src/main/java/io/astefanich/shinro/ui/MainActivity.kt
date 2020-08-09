package io.astefanich.shinro.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import dagger.android.support.DaggerAppCompatActivity
import io.astefanich.shinro.R

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.MODE_NIGHT_YES
    }

}