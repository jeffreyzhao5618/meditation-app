package com.cdtgrss.meditationapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.cdtgrss.meditationapp.databinding.FragmentTimerSettingsLengthBinding

class TimerLengthSettingsFragment : Fragment() {
    private lateinit var binding: FragmentTimerSettingsLengthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.
        inflate<FragmentTimerSettingsLengthBinding>(inflater, R.layout.fragment_timer_settings_length, container, false)

        // Setup number pickers
        setupNumPicker(binding.hourNumberPicker, 'h')
        setupNumPicker(binding.minuteNumberPicker, 'm')
        setupNumPicker(binding.secondNumberPicker, 's')

        return binding.root
    }

    /**
     * This function is used to setup the three number pickers used to change the timer's value.
     *
     * When number picker is changed get the old persisted string with key "timer_length"
     * and update it.
     *
     * Persisted value contained in shared preferences is a comma seperated string of the form
     * hours,minutes,seconds where:
     *      hours - number of hours in timer length
     *      minutes - number of minutes in timer length
     *      seconds - number of seconds in timer length
     *
     * When value of a number picker is changed update the persisted string in shared preferences
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
        val key = resources.getString(R.string.timer_length_key)
        val defaultValue = resources.getString(R.string.default_timer_length)
        numberPicker.apply {
            minValue = 0
            maxValue = 59
            displayedValues =
                resources.getStringArray(R.array.timer_settings_num_picker_string_array)
            value = PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(key, defaultValue)!!.split(',')[index].toInt()

            setOnValueChangedListener { _: NumberPicker, _: Int, newValue: Int ->
                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(activity)
                val oldPersistValue = sharedPreferences
                    .getString(key, defaultValue)!!.split(',').toMutableList()
                oldPersistValue[index] = newValue.toString()
                val newPersistValue = oldPersistValue.joinToString(",")
                sharedPreferences.edit().putString(key, newPersistValue).apply()
            }
        }
    }
}