package com.cdtgrss.meditationapp.screens.home

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.Ringtone
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.cdtgrss.meditationapp.AlarmReceiver
import com.cdtgrss.meditationapp.AlarmService
import com.cdtgrss.meditationapp.Constants
import com.cdtgrss.meditationapp.R
import com.cdtgrss.meditationapp.databinding.FragmentHomeBinding
import com.cdtgrss.meditationapp.observers.HideActionBarObserver
import com.cdtgrss.meditationapp.screens.timer.MyTimer
import kotlin.concurrent.timer

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
//    private lateinit var viewModelFactory: HomeViewModelFactory
//    private lateinit var sp: SharedPreferences

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Hide action bar
        lifecycle.addObserver(HideActionBarObserver(activity!!))

        // Inflate the layout for this fragment
        binding = DataBindingUtil.
            inflate(inflater, R.layout.fragment_home, container, false)

//        sp = PreferenceManager.getDefaultSharedPreferences(activity)
//
//        val timerLength = sp.getInt(Constants.KEY_TIMER_LENGTH, Constants.DEFAULT_TIMER_LENGTH)
//        val timerStarted = sp.getBoolean(Constants.KEY_TIMER_STARTED, false)
//
//        // If timer previously started navigate to TimerFragment
//        if (timerStarted) {
//            val action = HomeFragmentDirections.actionHomeFragmentToTimerFragment(timerLength, timerStarted)
//            findNavController().navigate(action)
//        }

        // Set timer text
//        binding.timerText.text = MyTimer.getTimeString(timerLength)
//        viewModelFactory = HomeViewModelFactory()

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.homeViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.eventTimerStart.observe(viewLifecycleOwner, { isFinished ->
            if (isFinished) {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToTimerFragment(viewModel.timerLength, viewModel.timerStarted)
                findNavController().navigate(action)
                viewModel.onTimerStartComplete()
            }
        })

        viewModel.eventTimerSettings.observe(viewLifecycleOwner, {isFinished ->
            if (isFinished) {
                findNavController().navigate(R.id.action_homeFragment_to_timerSettingsFragment)
                viewModel.onTimerSettingsComplete()
            }
        })

        Log.i("HomeFragment", "onCreateView")

        return binding.root
    }
}