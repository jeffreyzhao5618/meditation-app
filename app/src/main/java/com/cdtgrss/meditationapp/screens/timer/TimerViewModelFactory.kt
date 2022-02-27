package com.cdtgrss.meditationapp.screens.timer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cdtgrss.meditationapp.screens.home.HomeViewModel

class TimerViewModelFactory(
    private val application: Application,
    private val timerLength: Int,
    private val timerStarted: Boolean
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(application, timerLength, timerStarted) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}