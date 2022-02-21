package com.cdtgrss.meditationapp

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager

class AlarmReceiver() : BroadcastReceiver() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {


        val serviceIntent = Intent(context, AlarmService::class.java)
        context!!.startService(serviceIntent)

//        val startIntent = context!!.packageManager.getLaunchIntentForPackage(context.packageName)
//
//        startIntent!!.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or
//                Intent.FLAG_ACTIVITY_NEW_TASK or
//                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED

//        val startIntent: Intent = Intent().apply {
//            setClassName(context!!.packageName, MainActivity::class.java.name)
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//        }
//
//        context!!.startActivity(startIntent)

//        Log.i("AlarmReceiver", context.packageName)
        Log.i("AlarmReceiver", "test")

    }
}