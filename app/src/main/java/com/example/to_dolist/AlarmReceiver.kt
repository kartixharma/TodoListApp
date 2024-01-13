package com.example.to_dolist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val msg = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        createNotification(context!!, msg)
    }
    private fun createNotification(
        context: Context,
        msg: CharSequence?
    ){
        val notificationManager = baseApplication.notificationManager
        val notification = NotificationCompat.Builder(context,"channel id")
            .setContentTitle("Task Reminder🔔")
            .setContentText(msg)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(100, notification)
    }
}