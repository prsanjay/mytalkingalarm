package com.sanjay.mytalkingalarm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import java.util.*
import android.widget.Button
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.util.Log



import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.app.AlarmManager
import android.app.PendingIntent



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Set Alarm for 10 seconds from now (Test)
        val calendar = Calendar.getInstance() // 🔥 Set IST timezone
        //calendar.add(Calendar.SECOND, 10) // 🔥 Schedule for 10 seconds later
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 45)    // 30 minutes
        calendar.set(Calendar.SECOND, 0)     // 0 second

        scheduleAlarm(calendar.timeInMillis)
    }

    private fun scheduleAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 🔥 Set Exact Alarm in IST
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        Log.d("MainActivity", "Alarm scheduled for: ${Date(timeInMillis)}")
    }
}