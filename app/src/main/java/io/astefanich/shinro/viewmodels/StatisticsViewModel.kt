package io.astefanich.shinro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.Statistic
import io.astefanich.shinro.repository.StatisticsRepository
import javax.inject.Inject

class StatisticsViewModel
@Inject
constructor(
    val repo: StatisticsRepository
) : ViewModel() {

    private val _easyStats = MutableLiveData<Statistic>()
    val easyStats: LiveData<Statistic>
        get() = _easyStats

    private val _mediumStats = MutableLiveData<Statistic>()
    val mediumStats: LiveData<Statistic>
        get() = _mediumStats

    private val _hardStats = MutableLiveData<Statistic>()
    val hardStats: LiveData<Statistic>
        get() = _hardStats

    private val _allStats = MutableLiveData<Statistic>()
    val allStats: LiveData<Statistic>
        get() = _allStats

    init {
        _easyStats.value = Statistic(Difficulty.EASY, 10, 85f, 10, 12)
        _mediumStats.value = Statistic(Difficulty.MEDIUM, 35, 76.5f, 61, 75)
        _hardStats.value = Statistic(Difficulty.HARD, 20, 55.3f, 10000, 1000)
        _allStats.value = Statistic(Difficulty.HARD, 65, 73.999f, 1000, 1000)
    }
}