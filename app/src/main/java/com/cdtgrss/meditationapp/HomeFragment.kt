package com.cdtgrss.meditationapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.cdtgrss.meditationapp.databinding.FragmentHomeBinding


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    // Ringtone is played when timer finishes
    private lateinit var ringtone: Ringtone
    private var timer: CountDownTimer? = null
    private var timerValueUpdated: Boolean = false
    // These fields track the amount of time left on timer
    private var hours = 0
    private var minutes = 0
    private var seconds = 0

    private val timeInSeconds: Int
        get() = hours * 60 * 60 + minutes * 60 + seconds
    private val timeInMillis: Long
        get() = (timeInSeconds * 1000).toLong()

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.
            inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)

        // Disable action bar show hide animation
        (activity as AppCompatActivity).supportActionBar?.setShowHideAnimationEnabled(false)

        // Draw timer with values saved in shared preferences
//        resetTimer()
//        drawTimer()

        // Get ringtone based on uri saved in shared preferences
//        ringtone = RingtoneManager.getRingtone(activity,
//            Uri.parse(PreferenceManager.getDefaultSharedPreferences(activity)
//                    .getString(resources.getString(R.string.timer_sound_key),
//                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString())))

        binding.chip.visibility = View.VISIBLE

        // Button functionality
        binding.startButton.setOnClickListener { startButton: View ->
            startTimer()
            startButton.visibility = View.INVISIBLE
            binding.pauseButton.visibility = View.VISIBLE
            binding.stopButton.visibility = View.VISIBLE
            binding.resetTimerButton.visibility = View.VISIBLE
            binding.timerSettingsButton.visibility = View.INVISIBLE
        }

        binding.pauseButton.setOnClickListener { pauseButton: View ->
            pauseTimer()
            pauseButton.visibility = View.INVISIBLE
            binding.startButton.visibility = View.VISIBLE
        }

        binding.timerSettingsButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_timerSettingsFragment)
        }

        binding.resetTimerButton.setOnClickListener {
            stopTimer()
            resetTimer()
            drawTimer()
            timer = createCountDownTimer()
            binding.startButton.visibility = View.VISIBLE
            binding.pauseButton.visibility = View.INVISIBLE
        }

        binding.stopButton.setOnClickListener { stopButton: View ->
            stopTimer()
            resetTimer()
            drawTimer()
            timer = createCountDownTimer()
            stopButton.visibility = View.INVISIBLE
            binding.startButton.visibility = View.VISIBLE
            binding.timerSettingsButton.visibility = View.VISIBLE
            binding.pauseButton.visibility = View.INVISIBLE
            binding.resetTimerButton.visibility = View.INVISIBLE
        }

        binding.timerFinishedStopButton.setOnClickListener { timerFinishedStopButton: View ->
//            ringtone.stop()
            stopTimer()
            resetTimer()
            drawTimer()
            timer = createCountDownTimer()
            timerFinishedStopButton.visibility = View.INVISIBLE
            binding.startButton.visibility = View.VISIBLE
            binding.timerSettingsButton.visibility = View.VISIBLE
        }

        Log.i("HomeFragment", "onCreateView")

        return binding.root
    }

    /**
     * Show action bar when leaving home fragment
     */
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()

        resetTimer()
        drawTimer()

        Log.i("HomeFragment", "onResume")
    }

    /**
     * Show action bar when leaving home fragment
     */
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
        timer?.cancel()
        Log.i("HomeFragment", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("HomeFragment", "onDestroy")
    }

    // Set timer text based on timer length stored in shared preferences
    // Timer length string is of the form "hour,minute,second"
    private fun resetTimer() {
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)
        val isTimerStarted: Boolean = sp.getBoolean("timer_started", false)
        val isPaused: Boolean = sp.getBoolean("timer_started_paused", false)

        if (isTimerStarted) {
            hours = 0
            minutes = 0
            seconds = 0

            val endTime: Long = sp.getLong("timer_started_end_time", 0)
            if (System.currentTimeMillis() >= endTime && !isPaused) {
                binding.timerFinishedStopButton.visibility = View.VISIBLE
                binding.startButton.visibility = View.INVISIBLE
                binding.timerSettingsButton.visibility = View.INVISIBLE
                binding.pauseButton.visibility = View.INVISIBLE
                binding.resetTimerButton.visibility = View.INVISIBLE
                binding.stopButton.visibility = View.INVISIBLE

            } else if (System.currentTimeMillis() < endTime && !isPaused) {
                binding.pauseButton.visibility = View.VISIBLE
                binding.resetTimerButton.visibility = View.VISIBLE
                binding.stopButton.visibility = View.VISIBLE
                binding.startButton.visibility = View.INVISIBLE
                binding.timerSettingsButton.visibility = View.INVISIBLE

                val remainingTimeMillis = endTime - System.currentTimeMillis()
                var remainingTimeSeconds = (remainingTimeMillis / 1000).toInt()
                val secondsInHour = 3600
                if (remainingTimeSeconds >= secondsInHour) {
                    hours = remainingTimeSeconds / secondsInHour
                    remainingTimeSeconds -= hours * secondsInHour
                }
                if(remainingTimeSeconds >= 60) {
                    minutes = remainingTimeSeconds / 60
                    remainingTimeSeconds -= minutes * 60
                }
                seconds = remainingTimeSeconds

                timer = createCountDownTimer()
                timer?.start()

            } else if (isPaused) {
                binding.startButton.visibility = View.VISIBLE
                binding.resetTimerButton.visibility = View.VISIBLE
                binding.stopButton.visibility = View.VISIBLE
                binding.timerSettingsButton.visibility = View.INVISIBLE

                var remainingTimeSeconds = sp.getInt("timer_started_paused_time", 0)

                val secondsInHour = 3600
                if (remainingTimeSeconds >= secondsInHour) {
                    hours = remainingTimeSeconds / secondsInHour
                    remainingTimeSeconds -= hours * secondsInHour
                }
                if(remainingTimeSeconds >= 60) {
                    minutes = remainingTimeSeconds / 60
                    remainingTimeSeconds -= minutes * 60
                }
                seconds = remainingTimeSeconds

                timer = createCountDownTimer()
                drawTimer()
            }
        } else if (!isTimerStarted) {
            val timerLength = sp.getString(resources.getString(R.string.timer_length_key),
                    resources.getString(R.string.default_timer_length))!!
                .split(',')
            hours = timerLength[0].toInt()
            minutes = timerLength[1].toInt()
            seconds = timerLength[2].toInt()
            timer = createCountDownTimer()
        }

    }

    /**
     * Redraw timer based on the fields: hours, minutes, and seconds
     */
    private fun drawTimer() {
        binding.timerText.text = when {
            hours == 0 && minutes == 0 && seconds == 0 -> "DONE"
            hours != 0 ->
                "$hours:${minutes.toString().padStart(2,'0')}:" +
                        seconds.toString().padStart(2, '0')
            minutes != 0 ->
                "$minutes:${seconds.toString().padStart(2, '0')}"
            else -> "$seconds"
        }
    }

    /**
     * Start timer.
     * Set timer_started to true in shared preferences
     * Set timer end time in shared preferences
     */
    private fun startTimer() {
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)
        sp.edit().apply {
            putBoolean("timer_started", true).apply()
            putBoolean("timer_started_paused", false).apply()
            putLong("timer_started_end_time", System.currentTimeMillis() + timeInMillis).apply()
        }
//        scheduleAlarmClock()
        timer?.start()
    }

    /**
     * Pause timer.
     * Set timer_started_pause to true in shared preferences
     * Save remaining timer time in shared preferences
     */
    private fun pauseTimer() {
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)

        sp.edit().apply {
            putBoolean("timer_started", true).apply()
            putBoolean("timer_started_paused", true).apply()
            // Remaining time in timer in seconds
            putInt("timer_started_paused_time", timeInSeconds).apply()
        }

        timer?.cancel()
        timer = createCountDownTimer()
    }

    /**
     * Stop timer.
     * Set timer_started to false in shared preferences.
     */
    private fun stopTimer() {
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)
        sp.edit().apply {
            putBoolean("timer_started", false).apply()
            putBoolean("timer_started_paused", false).apply()
        }

//        cancelAlarmClock()
        timer?.cancel()
    }

    /**
     * Create a new CountDownTimer instance based on the fields: hours, minutes, seconds.
     * @return CountDownTimer instance that modifies hours, minutes, seconds on every
     *         tick to match remaining time on timer, and also redraws timer on every tick.
     */
    private fun createCountDownTimer() : CountDownTimer {
        var first = true
        return object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (first) first = false // Skip the first tick
                else {
                    when {
                        seconds != 0 -> seconds -= 1
                        minutes != 0 -> {
                            minutes -= 1
                            seconds += 59
                        }
                        hours != 0 -> {
                            hours -= 1
                            minutes += 59
                            seconds += 59
                        }
                    }
                    drawTimer()
                }
            }

            override fun onFinish() {
                binding.timerText.text = "DONE"
                binding.timerFinishedStopButton.visibility = View.VISIBLE
                binding.startButton.visibility = View.INVISIBLE
                binding.pauseButton.visibility = View.INVISIBLE
                binding.resetTimerButton.visibility = View.INVISIBLE
                binding.stopButton.visibility = View.INVISIBLE
            }
        }
    }

     @SuppressLint("UnspecifiedImmutableFlag")
     private fun scheduleAlarmClock() {
         val broadcastIntent : Intent = Intent(activity, AlarmReceiver::class.java).apply {
             action = "com.cdtgrss.broadcast.ACTION_ALARM"
         }

         val pendingIntent : PendingIntent
            = PendingIntent.getBroadcast(activity, 0, broadcastIntent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT)

         val alarmClockInfo =
             AlarmManager.AlarmClockInfo(System.currentTimeMillis() + timeInMillis, null)


        val alarmManager: AlarmManager
            = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

         alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
     }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun cancelAlarmClock() {
        val broadcastIntent : Intent = Intent(activity, AlarmReceiver::class.java).apply {
            action = "com.cdtgrss.broadcast.ACTION_ALARM"
        }

        val pendingIntent : PendingIntent
                = PendingIntent.getBroadcast(activity, 0, broadcastIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT)


        val alarmManager: AlarmManager
                = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
    }
}