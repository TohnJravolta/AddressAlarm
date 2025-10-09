package org.addressalarm.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", -1)
        if (notificationId != -1) {
            AlertManager.cancelReminders(notificationId)
        }
    }
}
