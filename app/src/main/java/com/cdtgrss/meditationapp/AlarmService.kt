package com.cdtgrss.meditationapp

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager

class AlarmService : Service() {

    private var ringtone: Ringtone? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!appInForeground(applicationContext)) {
            val fullScreenIntent = Intent(this, MainActivity::class.java)
            val fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder =
                NotificationCompat.Builder(this, "alarm_channel")
                    .setSmallIcon(R.drawable.ic_timer)
                    .setContentTitle("Time's up")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setSound(null)

                    // Use a full-screen intent only for the highest-priority alerts where you
                    // have an associated activity that you would like to launch after the user
                    // interacts with the notification. Also, if your app targets Android 10
                    // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                    // order for the platform to invoke this notification.
                    .setFullScreenIntent(fullScreenPendingIntent, true)

            val incomingCallNotification = notificationBuilder.build()

            startForeground(1337, incomingCallNotification)
        }

        Log.i("AlarmService", "onStartCommand")

        // Start playing ringtone
        ringtone = RingtoneManager.getRingtone(applicationContext,
            Uri.parse(
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    .getString(resources.getString(R.string.timer_sound_key),
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString())))
        ringtone?.play()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone?.stop()
        Log.i("ALarmService", "destroyed")
    }

    /**
     * Check if app is currently running
     *
     * Taken from:
     *  https://stackoverflow.com/questions/43378841/
     *      check-if-app-is-running-in-foreground-or-background-with-sync-adapter
     */
    private fun appInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses ?: return false
        return runningAppProcesses.any { it.processName == context.packageName && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND }
    }
}