package com.wingspan.adminpanel

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        // Set the app-wide light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}