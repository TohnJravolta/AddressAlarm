package org.flagdrive.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import org.flagdrive.match.MatchResult

object AlertManager {
    private const val CHANNEL_ID = "flagdrive_alerts"

    private fun ensureChannel(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = ctx.getSystemService(NotificationManager::class.java)
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "FlagDrive alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Heads-up notifications for detected matches"
                }
                nm.createNotificationChannel(channel)
            }
        }
    }

    fun notifyMatch(ctx: Context, match: MatchResult) {
        ensureChannel(ctx)

        val title = "⚑ ${match.place.name ?: match.place.rawAddress}"
        val details = "Tags: ${match.tagsSummary}  •  score ${match.score}"

        val notification: Notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(details)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(1001, notification)
    }
}
