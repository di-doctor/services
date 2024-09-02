package com.example.services

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService : JobService() {
    private val scope = CoroutineScope(Dispatchers.Main)

    private fun log(text: String) {
        Log.d("MY_JOB_TAG", text)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        scope.launch {
            for (i in 0 until 100) {
                delay(1000)
                log("onStartJob $i")
            }
            jobFinished(params, true)
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        scope.cancel()
    }
    companion object{
        const val JOB_SERVICE_INFO = 10
    }
}
