package com.cdtgrss.meditationapp.screens.timer

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData

class MyTimer {

    companion object {

        /**
         * @param timeInSeconds number of seconds
         * @return array of integers [hours,minutes,seconds]
         */
        fun getTimeArray(timeInSeconds: Int) : Array<Int> {
            var remainingSeconds = timeInSeconds

            val hours = timeInSeconds / 3600
            remainingSeconds -= hours * 3600
            val minutes = timeInSeconds / 60
            remainingSeconds -= minutes * 60
            val seconds = remainingSeconds

            return arrayOf(hours,minutes,seconds)

        }

        /**
         * @param timeInSeconds number of seconds
         * @return formatted string "hours:minutes:seconds"
         */
        fun getTimeString(timeInSeconds: Int) : String {
            val timeArray = getTimeArray(timeInSeconds)
            val hours = timeArray[0]
            val minutes = timeArray[1]
            val seconds = timeArray[2]

            return when {
                hours == 0 && minutes == 0 && seconds == 0 -> "DONE"
                hours != 0 ->
                    "$hours:${minutes.toString().padStart(2,'0')}:" +
                            seconds.toString().padStart(2, '0')
                minutes != 0 ->
                    "$minutes:${seconds.toString().padStart(2, '0')}"
                else -> "$seconds"
            }
        }

    }
}