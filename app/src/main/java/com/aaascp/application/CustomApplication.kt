package com.aaascp.application

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.aaascp.commons.initImageLoader
import com.aaascp.commons.initLogger
import com.aaascp.commons.initLoggerForDebug
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
        initImageLoader(this)
    }
}

private fun initTimber() {
    if (BuildConfig.DEBUG) {
        initLoggerForDebug()
    } else {
        initLogger()
    }
}
