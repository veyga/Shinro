package io.astefanich.shinro.ui

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import io.astefanich.shinro.R
import io.astefanich.shinro.repository.BoardRepository
import timber.log.Timber
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var repository: BoardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.i("repo is null? ${repository == null}")
        Timber.i(repository.getBoardById(0).toString())
    }

}