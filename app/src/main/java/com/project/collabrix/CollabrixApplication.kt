package com.project.collabrix

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CollabrixApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any other dependencies here if needed
    }
} 