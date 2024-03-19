package com.example.cameraxtestproj

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CameraAppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}