package com.example.to_dolist

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService

class baseApplication: Application() {
    companion object{
        lateinit var notificationManager: NotificationManager
    }
    override fun onCreate() {
        super.onCreate()
        val notChannel = NotificationChannel(
            "channel id",
            "channel name",
            NotificationManager.IMPORTANCE_HIGH
        )
        notChannel.enableVibration(true)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notChannel)
    }
}