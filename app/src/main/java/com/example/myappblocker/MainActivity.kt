package com.example.myappblocker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("BlockerPrefs", Context.MODE_PRIVATE)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(50, 50, 50, 50)
        }

        val title = TextView(this).apply {
            text = "Instagram & YouTube Blocker"
            textSize = 22f
            gravity = Gravity.CENTER
        }

        val statusText = TextView(this).apply {
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 30)
        }

        val inputMinutes = EditText(this).apply {
            hint = "Enter Lock Time in Minutes (e.g. 60)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        val btnStartLock = Button(this).apply {
            text = "Start Timer Lock"
        }

        val btnPermission = Button(this).apply {
            text = "1. Enable Accessibility Service"
        }

        fun updateStatus() {
            val unlockTime = prefs.getLong("unlock_time", 0L)
            if (System.currentTimeMillis() < unlockTime) {
                val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                statusText.text = "LOCKED until:\n${sdf.format(Date(unlockTime))}"
                btnStartLock.isEnabled = false
                inputMinutes.isEnabled = false
            } else {
                statusText.text = "Status: UNLOCKED"
                btnStartLock.isEnabled = true
                inputMinutes.isEnabled = true
            }
        }

        btnPermission.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        btnStartLock.setOnClickListener {
            val mins = inputMinutes.text.toString().toLongOrNull()
            if (mins != null && mins > 0) {
                val unlockTime = System.currentTimeMillis() + (mins * 60 * 1000)
                prefs.edit().putLong("unlock_time", unlockTime).apply()
                Toast.makeText(this, "Apps Locked for $mins minutes!", Toast.LENGTH_LONG).show()
                updateStatus()
            } else {
                Toast.makeText(this, "Please enter valid minutes", Toast.LENGTH_SHORT).show()
            }
        }

        layout.addView(title)
        layout.addView(statusText)
        layout.addView(btnPermission)
        layout.addView(inputMinutes)
        layout.addView(btnStartLock)

        setContentView(layout)
        updateStatus()
    }
}
