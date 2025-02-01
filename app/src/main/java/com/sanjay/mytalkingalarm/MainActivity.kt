package com.sanjay.mytalkingalarm

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import java.util.*
import android.content.Context
import android.util.Log
import android.widget.Toast



import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import android.app.AlarmManager
import android.app.PendingIntent



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlarmScreen()
        }
    }

    @Composable
    fun AlarmScreen() {
        val context = LocalContext.current
        var selectedHour by remember { mutableStateOf("") }
        var selectedMinute by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }
        var repeatCount by remember { mutableStateOf("1") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Set Alarm", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Time Picker (User enters hour and minute)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Hour: ")
                BasicTextField(
                    value = selectedHour,
                    onValueChange = { if (it.length <= 2) selectedHour = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(50.dp)
                        .background(Color.LightGray)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Minute: ")
                BasicTextField(
                    value = selectedMinute,
                    onValueChange = { if (it.length <= 2) selectedMinute = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(50.dp)
                        .background(Color.LightGray)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Message Input Field
            Text("Message:")
            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Repeat Count Input Field
            Text("Repeat Count:")
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .background(Color.LightGray)
                    .padding(8.dp)
            ) {
                BasicTextField(
                    value = repeatCount,
                    onValueChange = { if (it.length <= 2) repeatCount = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to Set Alarm
            Button(
                onClick = {
                    val hour = selectedHour.toIntOrNull() ?: 7
                    val minute = selectedMinute.toIntOrNull() ?: 30
                    val repeat = repeatCount.toIntOrNull() ?: 1
                    setAlarm(context, hour, minute, message, repeat)
                }
            ) {
                Text("Set Alarm")
            }
        }
    }

    private fun setAlarm(context: Context, hour: Int, minute: Int, message: String, repeat: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("message", message)
        intent.putExtra("repeat", repeat)

        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Alarm set for $hour:$minute", Toast.LENGTH_SHORT).show()
    }
}