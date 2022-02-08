package com.cdtgrss.meditationapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import com.cdtgrss.meditationapp.databinding.FragmentTimerSettingsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TimerSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerSettingsFragment : Fragment() {

    lateinit var binding: FragmentTimerSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer_settings, container, false)

        // Setup number pickers
        setupNumPicker(binding.hourNumberPicker)
        setupNumPicker(binding.minuteNumberPicker)
        setupNumPicker(binding.secondNumberPicker)

        // Inflate the layout for this fragment
        return binding.root
    }

    /**
     * This function is used to setup the three number pickers used to change the timer's value.
     *
     * @param numberPicker The NumberPicker to setup
     */
    private fun setupNumPicker (numberPicker: NumberPicker) {
        numberPicker.apply {
            minValue = 0
            maxValue = 59
            displayedValues =
                resources.getStringArray(R.array.timer_settings_num_picker_string_array)
        }
    }

}