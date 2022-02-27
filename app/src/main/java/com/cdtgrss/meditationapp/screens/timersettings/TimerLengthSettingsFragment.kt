package com.cdtgrss.meditationapp.screens.timersettings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.cdtgrss.meditationapp.Constants
import com.cdtgrss.meditationapp.R
import com.cdtgrss.meditationapp.databinding.FragmentTimerSettingsLengthBinding
import com.cdtgrss.meditationapp.screens.timer.MyTimer

class TimerLengthSettingsFragment : Fragment() {
    private lateinit var binding: FragmentTimerSettingsLengthBinding
    private lateinit var sp: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.
            inflate<FragmentTimerSettingsLengthBinding>(inflater,
                R.layout.fragment_timer_settings_length, container, false)

        sp = PreferenceManager.getDefaultSharedPreferences(activity)

        // Setup number pickers
        setupNumPicker(binding.hourNumberPicker, 'h')
        setupNumPicker(binding.minuteNumberPicker, 'm')
        setupNumPicker(binding.secondNumberPicker, 's')

        return binding.root
    }

    /**
     * This function is used to setup the three number pickers used to change the timer's value.
     *
     * @param numberPicker The NumberPicker to setup
     * @param type char that is used to figure out key of persisted int to get
     *      'h' - look at hours
     *      'm' - look at minutes
     *      else - look at seconds
     */
    private fun setupNumPicker (numberPicker: NumberPicker, type: Char) {
        val index = when(type) {
            'h' -> 0
            'm' -> 1
            else -> 2
        }
        val timeArray =
            MyTimer.getTimeArray(sp.getInt(Constants.KEY_TIMER_LENGTH, Constants.DEFAULT_TIMER_LENGTH))

        numberPicker.apply {
            minValue = 0
            maxValue = 59
            displayedValues =
                resources.getStringArray(R.array.timer_settings_num_picker_string_array)
            value = timeArray[index]
        }
    }

    /**
     * When fragment is stopped calculate the total number of seconds displayed by
     * the three number pickers and write to shared preferences
     */
    override fun onStop() {
        super.onStop()
        val totalSeconds = binding.hourNumberPicker.value * 3600 +
            binding.minuteNumberPicker.value * 60 +
            binding.secondNumberPicker.value
        sp.edit().putInt(Constants.KEY_TIMER_LENGTH, totalSeconds).commit()
    }
}