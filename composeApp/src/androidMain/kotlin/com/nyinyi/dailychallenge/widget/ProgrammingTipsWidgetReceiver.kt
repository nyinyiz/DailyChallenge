package com.nyinyi.dailychallenge.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class ProgrammingTipsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ProgrammingTipsWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        schedulePeriodicUpdates(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        WorkManager.getInstance(context).cancelUniqueWork(WIDGET_UPDATE_WORK_NAME)
    }

    companion object {
        private const val WIDGET_UPDATE_WORK_NAME = "programming_tips_widget_update"

        fun schedulePeriodicUpdates(context: Context) {
            val constraints =
                Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()

            val updateRequest =
                PeriodicWorkRequestBuilder<WidgetUpdateWorker>(1, TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .build()

            WorkManager
                .getInstance(context)
                .enqueueUniquePeriodicWork(
                    WIDGET_UPDATE_WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    updateRequest,
                )
        }
    }
}

class WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result =
        try {
            val manager = GlanceAppWidgetManager(applicationContext)
            val glanceIds = manager.getGlanceIds(ProgrammingTipsWidget::class.java)
            glanceIds.forEach { glanceId ->
                ProgrammingTipsWidget().update(applicationContext, glanceId)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
}
