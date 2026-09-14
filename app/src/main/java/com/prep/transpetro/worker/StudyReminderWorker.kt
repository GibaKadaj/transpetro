package com.prep.transpetro.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.prep.transpetro.R
import com.prep.transpetro.data.repository.DailyRecordRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@HiltWorker
class StudyReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dailyRecordRepository: DailyRecordRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val today = LocalDate.now().toString()
        val record = dailyRecordRepository.getByDate(today)
        if (record != null && record.hoursStudied > 0f) return Result.success()

        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = CHANNEL_ID
        if (nm.getNotificationChannel(channelId) == null) {
            nm.createNotificationChannel(
                NotificationChannel(channelId, "Lembretes de estudo", NotificationManager.IMPORTANCE_DEFAULT)
            )
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Transpetro Prep")
            .setContentText("Hora de estudar! Voce ainda nao registrou sessao hoje.")
            .setAutoCancel(true)
            .build()

        nm.notify(NOTIFICATION_ID, notification)
        return Result.success()
    }

    companion object {
        const val CHANNEL_ID = "study_reminders"
        const val NOTIFICATION_ID = 1001
        const val WORK_NAME = "daily_study_reminder"

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<StudyReminderWorker>(1, TimeUnit.DAYS)
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
