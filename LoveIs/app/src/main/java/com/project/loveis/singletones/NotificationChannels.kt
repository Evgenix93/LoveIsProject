package com.project.loveis.singletones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.icu.util.VersionInfo
import android.os.Build
import androidx.core.app.NotificationManagerCompat

object NotificationChannels {
    fun create(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(IMPORTANT_CHANNEL, "important notifications", NotificationManager.IMPORTANCE_HIGH)
            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }

    const val IMPORTANT_CHANNEL = "important channel"
}