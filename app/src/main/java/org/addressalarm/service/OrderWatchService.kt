package org.addressalarm.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.addressalarm.di.ServiceLocator
import org.addressalarm.match.AddressMatcher
import org.addressalarm.match.MatchResult

class OrderWatchService : AccessibilityService() {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var lastNotifiedPlaceId: Long? = null
    private var lastNotifiedAtMs: Long = 0L

    companion object {
        private const val MIN_NOTIFY_INTERVAL_MS = 20_000L // 20s
    }

    override fun onServiceConnected() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageName = event?.packageName?.toString() ?: return
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val selectedApps = sharedPrefs.getStringSet("selected_apps", emptySet()) ?: emptySet()

        if (packageName !in selectedApps) {
            return
        }

        val root = rootInActiveWindow ?: return
        val text = collectText(root)

        scope.launch {
            val repository = ServiceLocator.repository(applicationContext)
            val places = repository.getAllFlow().first()

            val match: MatchResult = AddressMatcher.check(
                screenText = text,
                places = places
            ) ?: return@launch

            val now = System.currentTimeMillis()
            val samePlace = (lastNotifiedPlaceId == match.place.id)
            val tooSoon = (now - lastNotifiedAtMs) < MIN_NOTIFY_INTERVAL_MS
            if (samePlace && tooSoon) return@launch

            lastNotifiedPlaceId = match.place.id
            lastNotifiedAtMs = now
            AlertManager.notifyMatch(
                ctx = this@OrderWatchService,
                match = match
            )
        }
    }

    override fun onInterrupt() {}

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
