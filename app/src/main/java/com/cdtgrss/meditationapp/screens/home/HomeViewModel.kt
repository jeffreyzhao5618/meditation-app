package com.cdtgrss.meditationapp.screens.home

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.cdtgrss.meditationapp.Constants
import com.cdtgrss.meditationapp.SharedPreferenceIntLiveData
import com.cdtgrss.meditationapp.screens.timer.MyTimer

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private var sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String>
            get() = _timerText

    private val _eventTimerStart = MutableLiveData<Boolean>()
    val eventTimerStart: LiveData<Boolean>
        get() = _eventTimerStart

    private val _eventTimerSettings = MutableLiveData<Boolean>()
    val eventTimerSettings: LiveData<Boolean>
        get() = _eventTimerSettings

    val timerStarted: Boolean = sp.getBoolean(Constants.KEY_TIMER_STARTED, false)
    private val _timerLength: SharedPreferenceIntLiveData
        = SharedPreferenceIntLiveData(sp, Constants.KEY_TIMER_LENGTH, Constants.DEFAULT_TIMER_LENGTH)
    val timerLength: Int
        get() = _timerLength.value ?: 0
    val timerLengthString = Transformations.map(_timerLength) { time ->
        MyTimer.getTimeString(time)
    }

    init {
        if (timerStarted)
            _eventTimerStart.value = true

        Log.i("HomeViewModel", "init")
    }

    fun onTimerStart() {
        _eventTimerStart.value = true
    }

    fun onTimerStartComplete() {
        _eventTimerStart.value = false
    }

    fun onTimerSettings() {
        _eventTimerSettings.value = true
    }

    fun onTimerSettingsComplete() {
        _eventTimerSettings.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("HomeViewModel", "onCleared")
    }

}