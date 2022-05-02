package com.project.loveis

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.project.loveis.singletones.NotificationChannels

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        NotificationChannels.create(this)
    }
}