package com.cdtgrss.meditationapp.screens.timer

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.cdtgrss.meditationapp.Constants

class TimerViewModel(
    application: Application,
    private var timerLength: Int,
    timerStarted: Boolean
) : AndroidViewModel(application) {

    private val sp = PreferenceManager.getDefaultSharedPreferences(application)

    private var timer: CountDownTimer? = null

    private val timerSeconds = MutableLiveData<Int>()
    val timerSecondsString = Transformations.map(timerSeconds) { time ->
        if (time > 0) {
            MyTimer.getTimeString(time)
        } else {
            "DONE"
        }
    }
    private val timerMillis: Long
        get() = timerSeconds.value?.toLong()?.times( 1000L) ?: 0

    private val _startButtonVisibility = MutableLiveData<Int>()
    val startButtonVisibility: LiveData<Int>
        get() = _startButtonVisibility

    private val _pauseButtonVisibility = MutableLiveData<Int>()
    val pauseButtonVisibility: LiveData<Int>
        get() = _pauseButtonVisibility

    private val _finishButtonVisibility = MutableLiveData<Int>()
    val finishButtonVisibility: LiveData<Int>
        get() = _finishButtonVisibility

    private val _stopButtonVisibility = MutableLiveData<Int>()
    val stopButtonVisibility: LiveData<Int>
        get() = _stopButtonVisibility

    private val _resetButtonVisibility = MutableLiveData<Int>()
    val resetButtonVisibility: LiveData<Int>
        get() = _resetButtonVisibility

    private val _eventStart = MutableLiveData<Boolean>()
    val eventStart: LiveData<Boolean>
        get() = _eventStart

    private val _eventPause = MutableLiveData<Boolean>()
    val eventPause: LiveData<Boolean>
        get() = _eventPause

    private val _eventFinish = MutableLiveData<Boolean>()
    val eventFinish: LiveData<Boolean>
        get() = _eventFinish

    private val _eventStop = MutableLiveData<Boolean>()
    val eventStop: LiveData<Boolean>
        get() = _eventStop

    private val _eventReset = MutableLiveData<Boolean>()
    val eventReset: LiveData<Boolean>
        get() = _eventReset

    init {
        val timerEndTime = sp.getLong(Constants.KEY_TIMER_END_TIME, 0L)
        val timerPaused = sp.getBoolean(Constants.KEY_TIMER_PAUSED, false)
        val timerPausedTime = sp.getInt(Constants.KEY_TIMER_PAUSED_TIME, 0)

        if (timerStarted) {
            if (!timerPaused && System.currentTimeMillis() >= timerEndTime) {
                // Timer finished
                changeButtonVisibility(finishButton = View.VISIBLE)
                timerSeconds.value = 0
            } else if (!timerPaused && System.currentTimeMillis() < timerEndTime) {
                // Timer ongoing
                val remainingTimeMillis = timerEndTime - System.currentTimeMillis()
                val remainingTimeSeconds = (remainingTimeMillis / 1000).toInt()
                timerSeconds.value = remainingTimeSeconds
                onStart()

            } else if (timerPaused) {
                // Timer paused
                timerSeconds.value = timerPausedTime
                onPause()
            }
        } else if (!timerStarted) {
            // New Timer
            timerSeconds.value = timerLength
            onStart()
        }

    }
    fun onStart() {
        sp.edit().apply {
            putBoolean(Constants.KEY_TIMER_STARTED, true).apply()
            putLong(Constants.KEY_TIMER_END_TIME, System.currentTimeMillis() + timerMillis).apply()
            putBoolean(Constants.KEY_TIMER_PAUSED, false).apply()
        }

        changeButtonVisibility(
            pauseButton = View.VISIBLE,
            stopButton = View.VISIBLE,
            resetButton = View.VISIBLE
        )

        createCountdownTimer()
        timer?.start()
        _eventStart.value = true
    }

    fun onStartCompleted() {
        _eventStart.value = false
    }

    fun onPause() {
        sp.edit().apply {
            putBoolean(Constants.KEY_TIMER_STARTED, true).apply()
            putBoolean(Constants.KEY_TIMER_PAUSED, true).apply()
            // Remaining time in timer in seconds
            putInt(Constants.KEY_TIMER_PAUSED_TIME, timerSeconds.value ?: 0).apply()
        }

        changeButtonVisibility(
            startButton = View.VISIBLE,
            stopButton = View.VISIBLE,
            resetButton = View.VISIBLE
        )
        timer?.cancel()
        _eventPause.value = true
    }

    fun onPauseCompleted() {
        _eventPause.value = false
    }

    fun onStop() {
        sp.edit().apply {
            putBoolean(Constants.KEY_TIMER_STARTED, false).apply()
            putBoolean(Constants.KEY_TIMER_PAUSED, false).apply()
        }

        timer?.cancel()
        _eventStop.value = true
    }

    fun onStopComplete() {
        _eventStop.value = false
    }

    fun onReset() {
        sp.edit().apply {
            putBoolean(Constants.KEY_TIMER_STARTED, false).apply()
            putBoolean(Constants.KEY_TIMER_PAUSED, false).apply()
        }

        changeButtonVisibility(
            startButton = View.VISIBLE,
            stopButton = View.VISIBLE,
            resetButton = View.VISIBLE
        )

        timerSeconds.value = timerLength
        timer?.cancel()
    }

    private fun createCountdownTimer() {
        val timeInMillis = timerSeconds.value?.times(1000L)
        var first = true
        timer = object : CountDownTimer(timeInMillis ?: 0, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                if (first) first = false // Skip the first tick
                else
                    timerSeconds.value = timerSeconds.value?.minus(1)
            }

            override fun onFinish() {
                timerSeconds.value = 0
                changeButtonVisibility(finishButton = View.VISIBLE)
            }
        }
    }

    private fun changeButtonVisibility(
        startButton: Int = View.INVISIBLE,
        pauseButton: Int = View.INVISIBLE,
        finishButton: Int = View.INVISIBLE,
        stopButton: Int = View.INVISIBLE,
        resetButton: Int = View.INVISIBLE
    ) {
        _startButtonVisibility.value = startButton
        _pauseButtonVisibility.value = pauseButton
        _finishButtonVisibility.value = finishButton
        _stopButtonVisibility.value = stopButton
        _resetButtonVisibility.value = resetButton
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}