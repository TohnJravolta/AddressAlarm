package org.addressalarm.service

import android.content.Context
import android.provider.Settings
import android.widget.Toast

fun isAccessibilityEnabled(ctx: Context): Boolean {
    return try {
        Settings.Secure.getInt(ctx.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED) == 1
    } catch (e: Exception) { false }
}

fun ensureAccessibilityEnabled(ctx: Context) {
    Toast.makeText(ctx, "Go to Settings → Accessibility and enable AddressAlarm", Toast.LENGTH_LONG).show()
}
