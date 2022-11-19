package com.techblue.virginmoney

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HostApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}