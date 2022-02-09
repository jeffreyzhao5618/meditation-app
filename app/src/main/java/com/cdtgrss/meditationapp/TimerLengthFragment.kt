package com.cdtgrss.meditationapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.cdtgrss.meditationapp.databinding.FragmentTimerLengthBinding

class TimerLengthFragment : Fragment() {
    private lateinit var binding: FragmentTimerLengthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.
        inflate<FragmentTimerLengthBinding>(inflater, R.layout.fragment_timer_length, container, false)

        // Setup number pickers
        setupNumPicker(binding.hourNumberPicker)
        setupNumPicker(binding.minuteNumberPicker)
        setupNumPicker(binding.secondNumberPicker)

        return binding.root
    }

    private fun setupNumPicker (numberPicker: NumberPicker) {
//        val index = when(type) {
//            "h" -> 0
//            "m" -> 1
//            else -> 2
//        }
        numberPicker.apply {
            minValue = 0
            maxValue = 59
            displayedValues =
                resources.getStringArray(R.array.timer_settings_num_picker_string_array)
            setOnValueChangedListener { _: NumberPicker, _: Int, newValue: Int ->

            }
        }
    }
}