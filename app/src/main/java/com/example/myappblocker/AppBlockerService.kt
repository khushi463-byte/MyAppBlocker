package com.example.myappblocker

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast

class AppBlockerService : AccessibilityService() {

    private val blockedApps = listOf(
        "com.instagram.android",
        "com.google.android.youtube"
    )

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            val prefs = getSharedPreferences("BlockerPrefs", Context.MODE_PRIVATE)
            val unlockTime = prefs.getLong("unlock_time", 0L)

            if (System.currentTimeMillis() < unlockTime && blockedApps.contains(packageName)) {
                Toast.makeText(this, "Timer Lock Active! Instagram & YouTube Blocked.", Toast.LENGTH_SHORT).show()
                performGlobalAction(GLOBAL_ACTION_HOME)
            }
        }
    }

    override fun onInterrupt() {}
}
