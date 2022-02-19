package com.cdtgrss.meditationapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.cdtgrss.meditationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)

        // Enable up button
        NavigationUI.setupActionBarWithNavController(this, navController)

        // Notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "This is the name"
            val descriptionText = "This is a description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel("alarm_channel", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        Log.i("MainActivity", supportActionBar.toString())

        // For debugging
//        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}