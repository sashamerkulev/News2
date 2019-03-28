package ru.merkulyevsasha.news.newsjobs

import android.os.Build
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import javax.inject.Inject

class NewsWorkerRunner @Inject constructor() : BackgroundWorker {

    override fun runWorker(navId: Int, searchText: String?) {

        val data: Data = Data.Builder()
            .putBoolean("singleRun", true)
            .putInt("navId", navId)
            .putString("searchText", searchText)
            .build()
        val constraints =
            Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            constraints
                .setRequiresDeviceIdle(false)
        }
        val worker = OneTimeWorkRequestBuilder<NewsWorker2>()
            .setConstraints(constraints.build())
            .setInputData(data)
            .addTag(NewsWorker2::class.java.simpleName)
            .build()
        WorkManager.getInstance().enqueue(worker)
    }
}