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

//        Logger.d("This is a debug log message.")
//        Logger.e("This is an error log with a tag.", "ErrorTag")
//        Logger.w("This is a warning log.")
//        Logger.v("This is a verbose log.")
//        Logger.i("This is an info log.")
//        Logger.wtf("What a Terrible Failure!")

//        val json = "{ \"name\":\"John\", \"age\":30, \"city\":\"New York\" }"
//        Logger.json(json)
    }

    private fun initLogger() {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}