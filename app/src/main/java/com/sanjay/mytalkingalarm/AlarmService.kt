package com.sanjay.mytalkingalarm

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class AlarmService : Service(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var isTTSInitialized = false // ✅ Track if TTS is ready

    override fun onCreate() {
        super.onCreate()
        Log.d("AlarmService", "Service created")
        createNotificationChannel()

        tts = TextToSpeech(this, this) // 🔥 Initialize TTS here
        startForeground(1, getNotification())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val langResult = tts.setLanguage(Locale.US)
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported or missing data")
            } else {
                isTTSInitialized = true // ✅ Mark TTS as ready
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val message = it.getStringExtra("message") ?: "Time's up!"
            Log.d("AlarmService", "Received message: $message")

            if (isTTSInitialized) {
                speakMessage(message) // ✅ Speak immediately if ready
            } else {
                // ✅ Wait for TTS to initialize, then speak
                Thread {
                    while (!isTTSInitialized) {
                        Thread.sleep(100) // Wait for TTS to be ready
                    }
                    speakMessage(message)
                }.start()
            }
        }
        return START_NOT_STICKY
    }

    private fun speakMessage(message: String) {
        if (isTTSInitialized) {
            Log.d("AlarmService", "Speaking message: $message")
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            Log.e("TTS", "TTS is not initialized yet, cannot speak!")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
        Log.d("AlarmService", "TextToSpeech stopped and service destroyed")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alarm_channel",
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun getNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return Notification.Builder(this, "alarm_channel")
            .setContentTitle("Alarm is Running")
            .setContentText("Your alarm is speaking...")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use a valid drawable
            .setContentIntent(pendingIntent)
            .build()
    }
}
