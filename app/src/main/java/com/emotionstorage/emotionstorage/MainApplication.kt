package com.emotionstorage.emotionstorage

import android.app.Application
import com.emotionstorage.auth.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}