package com.cdtgrss.meditationapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.cdtgrss.meditationapp.databinding.FragmentHomeBinding
import com.cdtgrss.meditationapp.databinding.FragmentTimerSettingsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TimerSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerSettingsFragment : Fragment() {

    private lateinit var binding: FragmentTimerSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.
            inflate<FragmentTimerSettingsBinding>(inflater, R.layout.fragment_timer_settings, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

}