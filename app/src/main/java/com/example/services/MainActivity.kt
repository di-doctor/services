package com.example.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var page = 0
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.services.setOnClickListener {
            //startService(MyService.intentInit(this))
            stopService(MyForeGroundService.intentInit(this))
        }
        binding.foreground.setOnClickListener {
            ContextCompat.startForegroundService(this,MyForeGroundService.intentInit(this))
        }
        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this,MyJobService::class.java)

            val jobInfo = JobInfo.Builder(MyJobService.JOB_SERVICE_INFO,componentName)
                //.setRequiresCharging(true)
                //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)

        }
        binding.workManager.setOnClickListener {
            Log.d(
                MyWorker.LOG_NAME,
                "application - $application   applicationContex - $applicationContext"
            )

            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                MyWorker.WORK_NAME,
                ExistingWorkPolicy.APPEND,
                MyWorker.makeRequest(page = page++)
            )
        }
    }

    private fun showNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel  = NotificationChannel(
                ID_CHANNEL,
                NAME_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val notification = NotificationCompat.Builder(this, ID_CHANNEL)
            .setContentText("Text Message")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        notificationManager.notify(1,notification)

    }
    companion object{
        const val ID_CHANNEL = "id_channel"
        const val NAME_CHANNEL = "name_channel"
    }
}