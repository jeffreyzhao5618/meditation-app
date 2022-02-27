package com.cdtgrss.meditationapp

class Constants {
    companion object {
        // Keys for shared preferences
        const val KEY_TIMER_LENGTH = "timer_length" // in seconds
        const val KEY_TIMER_STARTED = "timer_started"
        const val KEY_TIMER_END_TIME = "timer_end_time" // in millis since epoch
        const val KEY_TIMER_PAUSED = "timer_paused"
        const val KEY_TIMER_PAUSED_TIME = "timer_paused_time" // in seconds

        // Default shared preferences values
        const val DEFAULT_TIMER_LENGTH = 300
    }
}