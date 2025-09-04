package org.flagdrive.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import org.flagdrive.R
import org.flagdrive.match.MatchResult

object AlertManager {
    private const val CHANNEL_ID = "flagdrive_alerts"

    private fun ensureChannel(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = ctx.getSystemService(NotificationManager::class.java)
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                val ch = NotificationChannel(
                    CHANNEL_ID,
                    "FlagDrive alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Heads-up notifications for detected matches"
                }
                nm.createNotificationChannel(ch)
            }
        }
    }

    fun notifyMatch(ctx: Context, match: MatchResult) {
        ensureChannel(ctx)
        val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notif: Notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setContentTitle("⚑ ${match.place.name ?: match.place.rawAddress}")
            .setContentText("Tags: ${match.tagsSummary} · score ${match.score}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .build()

        // Use a fresh ID so every match can heads-up.
        val id = (System.currentTimeMillis() and 0x7FFFFFFF).toInt()
        try {
            nm.notify(id, notif)
        } catch (_: SecurityException) {
            // Permission denied => no-op
        }
    }
}
