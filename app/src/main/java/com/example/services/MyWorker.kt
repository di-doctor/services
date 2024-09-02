package com.example.services

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(context: Context, private val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        val page = workerParameters.inputData.getInt(PAGE,0)
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("doWork $i - $page")
        }
        return Result.success()
    }

    private fun log(text: String) {
        Log.d(LOG_NAME, "My Worker $text")
    }

    companion object{
        const val LOG_NAME = "WORK_MANAGER"
        const val PAGE = "page"
        const val WORK_NAME = "workName"

        fun makeRequest(page:Int): OneTimeWorkRequest{
            return OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(workDataOf(PAGE to page))
                .setConstraints(makeConstraints())
                .build()
        }
        private fun makeConstraints():Constraints{
            return Constraints.Builder()
                .setRequiresCharging(false)
                //.setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
        }
    }
}

