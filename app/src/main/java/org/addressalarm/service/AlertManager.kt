package org.addressalarm.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import org.addressalarm.match.MatchResult
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object AlertManager {
    private const val CHANNEL_ID = "addressalarm_alerts"
    private const val MAX_REMINDERS = 6
    private const val REMINDER_DELAY_SECONDS = 3L

    private val scheduler = Executors.newScheduledThreadPool(1)
    private val reminderTasks = mutableMapOf<Int, ScheduledFuture<*>>()

    private fun ensureChannel(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = ctx.getSystemService(NotificationManager::class.java)
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "AddressAlarm alerts",
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

        val notificationId = match.place.id.toInt()
        val title = "⚑ ${match.place.name ?: match.place.rawAddress}"
        val details = "Tags: ${match.tagsSummary}  •  score ${match.score}"

        val deleteIntent = Intent(ctx, NotificationDismissReceiver::class.java).apply {
            putExtra("notificationId", notificationId)
        }
        val pendingDeleteIntent = PendingIntent.getBroadcast(
            ctx,
            notificationId,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(details)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDeleteIntent(pendingDeleteIntent)
            .build()

        val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(notificationId, notification)

        // Schedule reminders
        scheduleReminder(ctx, notificationId, notification)
    }

    private fun scheduleReminder(ctx: Context, notificationId: Int, notification: Notification) {
        var reminderCount = 0
        val task = scheduler.scheduleAtFixedRate({
            if (reminderCount < MAX_REMINDERS) {
                val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.notify(notificationId, notification)
                reminderCount++
            } else {
                cancelReminders(notificationId)
            }
        }, REMINDER_DELAY_SECONDS, REMINDER_DELAY_SECONDS, TimeUnit.SECONDS)
        reminderTasks[notificationId] = task
    }

    fun cancelReminders(notificationId: Int) {
        reminderTasks[notificationId]?.cancel(true)
        reminderTasks.remove(notificationId)
    }
}
