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
import org.flagdrive.match.MatchResult // ← unify on the match package

class OrderWatchService : AccessibilityService() {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // Debounce state
    private var lastNotifiedPlaceId: Long? = null
    private var lastNotifiedAtMs: Long = 0L

    companion object {
        private const val MIN_NOTIFY_INTERVAL_MS = 20_000L // 20s
    }

    override fun onServiceConnected() {
        // Service is ready
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val root = rootInActiveWindow ?: return
        val text = collectText(root)

        scope.launch {
            val db = AppDatabase.get(this@OrderWatchService)
            val places = db.places().getAll() // ← getALL -> getAll()

            // run the matcher
            val match: MatchResult = AddressMatcher.check(
                screenText = text,
                places = places
            ) ?: return@launch

            // debounce: same place too soon? skip
            val now = System.currentTimeMillis()
            val samePlace = (lastNotifiedPlaceId == match.place.id)
            val tooSoon = (now - lastNotifiedAtMs) < MIN_NOTIFY_INTERVAL_MS
            if (samePlace && tooSoon) return@launch

            // record + notify
            lastNotifiedPlaceId = match.place.id
            lastNotifiedAtMs = now
            AlertManager.notifyMatch(
                ctx = this@OrderWatchService, // pass Context, not CoroutineScope
                match = match
            )
        }
    }

    override fun onInterrupt() { }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private fun collectText(node: AccessibilityNodeInfo): String {
        val sb = StringBuilder()
        fun dfs(n: AccessibilityNodeInfo?) {
            if (n == null) return
            n.text?.let { sb.append(it).append('\n') }
            for (i in 0 until n.childCount) dfs(n.getChild(i))
        }
        dfs(node)
        return sb.toString()
    }
}
