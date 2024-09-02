package com.example.services

import android.annotation.SuppressLint
import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MyIntentService : IntentService("name") {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    private fun log(text: String) {
        Log.d(MY_FOREGROUND_SERVICE_TAG, text)
    }
    override fun onHandleIntent(intent: Intent?) {
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("onStartCommand $i")
        }
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                ID_CHANNEL,
                NAME_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }
    private fun createNotification():Notification {
        return NotificationCompat.Builder(this, ID_CHANNEL)
            .setContentText("Foreground Service Run")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    companion object {
        const val ID_CHANNEL = "id_channel"
        const val NAME_CHANNEL = "name_channel"
        const val NOTIFICATION_ID = 1
        const val MY_FOREGROUND_SERVICE_TAG = "MYSERVICE_TAG"

        fun intentInit(context: Context): Intent {
            return Intent(context, IntentService::class.java)
        }
    }
}
