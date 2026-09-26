package com.cravexa

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CravexaApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

