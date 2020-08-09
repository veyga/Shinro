package io.astefanich.shinro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.domain.Progress
import io.astefanich.shinro.repository.BoardRepository
import javax.inject.Inject

class ProgressViewModel @Inject constructor(val repo: BoardRepository) : ViewModel() {

    val progress: LiveData<List<Progress>> = repo.getProgress()
}