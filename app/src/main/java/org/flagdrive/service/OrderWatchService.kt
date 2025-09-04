package org.flagdrive.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.flagdrive.data.AppDatabase
import org.flagdrive.match.AddressMatcher

/**
 * Watches the screen (via Accessibility) and tries to match visible text
 * against your saved places. When a match is found, it posts a heads-up
 * notification through [AlertManager].
 */
class OrderWatchService : AccessibilityService() {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onServiceConnected() {
        // Service is ready
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val root = rootInActiveWindow ?: return
        val text = collectText(root)

        scope.launch {
            val db = AppDatabase.get(this@OrderWatchService)
            // NOTE: DAO function is getAll() (camelCase)
            val places = db.places().getAll()

            AddressMatcher.check(
                screenText = text,
                places = places
            )?.let { match ->
                // Pass the service Context, not the CoroutineScope
                AlertManager.notifyMatch(
                    ctx = this@OrderWatchService,
                    match = match
                )
            }
        }
    }

    override fun onInterrupt() {
        // No-op
    }

    override fun onDestroy() {
        // Avoid leaks
        scope.cancel()
        super.onDestroy()
    }

    private fun collectText(node: AccessibilityNodeInfo): String {
        val sb = StringBuilder()

        fun dfs(n: AccessibilityNodeInfo?) {
            if (n == null) return
            n.text?.let { sb.append(it).append('\n') }
            for (i in 0 until n.childCount) {
                dfs(n.getChild(i))
            }
        }

        dfs(node)
        return sb.toString()
    }
}
