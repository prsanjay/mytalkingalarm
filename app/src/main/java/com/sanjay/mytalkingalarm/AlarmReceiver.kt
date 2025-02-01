// AlarmReceiver.kt
package com.sanjay.mytalkingalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarm received, triggering AlarmService")

        // Start the AlarmService to handle the speaking task
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra("message", "Wake up! It's time!")
        context.startForegroundService(serviceIntent)
    }
}
