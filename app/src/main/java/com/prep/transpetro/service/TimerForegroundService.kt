package com.prep.transpetro.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.prep.transpetro.MainActivity
import com.prep.transpetro.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimerForegroundService : Service() {

    companion object {
        const val CHANNEL_ID = "timer_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_START = "action_start"
        const val ACTION_STOP = "action_stop"
        const val EXTRA_BLOCK_NAME = "extra_block_name"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val blockName = intent.getStringExtra(EXTRA_BLOCK_NAME) ?: "Estudo"
                startForeground(NOTIFICATION_ID, buildNotification(blockName))
            }
            ACTION_STOP -> stopSelf()
        }
        return START_NOT_STICKY
    }

    private fun buildNotification(blockName: String): Notification {
        val openIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Sessão ativa: $blockName")
            .setContentText("Toque para voltar ao app")
            .setContentIntent(openIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Timer de Estudo",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}
