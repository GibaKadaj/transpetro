package com.prep.transpetro.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prep.transpetro.worker.StudyReminderWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            StudyReminderWorker.schedule(context)
        }
    }
}
