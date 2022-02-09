package com.cdtgrss.meditationapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.cdtgrss.meditationapp.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    // These fields track the amount of time left on timer
    private var hours = 0
    private var minutes = 0
    private var seconds = 0

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.
            inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)

        binding.timerText.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_timerSettingsFragment)
        }

        // Disable action bar show hide animation
        (activity as AppCompatActivity).supportActionBar?.setShowHideAnimationEnabled(false)

        return binding.root
    }

    /**
     * Show action bar when leaving home fragment
     */
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    /**
     * Show action bar when leaving home fragment
     */
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    /**
     * Set timer text based on timer length stored in shared preferences
     * Timer length string is of the form "hour,minute,second"
     */
    override fun onStart() {
        super.onStart()
        val timerLength = PreferenceManager.getDefaultSharedPreferences(activity)
            .getString(resources.getString(R.string.timer_length_key),
                resources.getString(R.string.default_timer_length))!!
            .split(',')
        hours = timerLength[0].toInt()
        minutes = timerLength[1].toInt()
        seconds = timerLength[2].toInt()
        drawTimer()
    }

    private fun drawTimer() {
        binding.timerText.text = when {
            hours != 0 ->
                "$hours:${minutes.toString().padStart(2,'0')}:" +
                        "${seconds.toString().padStart(2, '0')}"
            minutes != 0 ->
                "$minutes:${seconds.toString().padStart(2, '0')}"
            else -> "$seconds"
        }
    }
}