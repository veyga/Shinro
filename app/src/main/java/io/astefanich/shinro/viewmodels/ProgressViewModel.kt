package io.astefanich.shinro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Progress
import io.astefanich.shinro.repository.BoardRepository
import timber.log.Timber
import javax.inject.Inject

class ProgressViewModel @Inject constructor(val repo: BoardRepository) : ViewModel() {

//    fun progress(): LiveData<List<Progress>>{
//        Timber.i("repo injected in progress viewmodel")
//        return repo.getProgress
//    }
//    val progress = repo.getProgress()
    val progress: LiveData<List<Progress>> = repo.getProgress()
}