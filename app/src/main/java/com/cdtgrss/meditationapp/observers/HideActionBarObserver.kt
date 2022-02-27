package com.cdtgrss.meditationapp.observers

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * LifecycleObserver that observes a LifeCycleOwner.
 * When the owner resumes the action bar is hidden and when
 * the owner stops the action bar is unhidden
 */
class HideActionBarObserver(private val activity: FragmentActivity) : DefaultLifecycleObserver {

    @SuppressLint("RestrictedApi")
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        (activity as AppCompatActivity).supportActionBar?.setShowHideAnimationEnabled(false)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}