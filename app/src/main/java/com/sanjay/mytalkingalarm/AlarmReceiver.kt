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
        val message = intent.getStringExtra("message") ?: "Wake up! It's time!"
        val repeatCount = intent.getIntExtra("repeat", 1)

        serviceIntent.putExtra("message", message)
        serviceIntent.putExtra("repeat", repeatCount)
        context.startForegroundService(serviceIntent)
    }
}
