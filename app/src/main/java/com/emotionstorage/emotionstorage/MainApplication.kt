package com.emotionstorage.emotionstorage

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
        initKakaoSDK()
    }

    private fun initLogger() {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        }
        )
    }

    private fun initKakaoSDK() {
        try {
            KakaoSdk.init(this@MainApplication, BuildConfig.KAKAO_NATIVE_APP_KEY)
            Logger.i("kakao sdk init success")
        } catch (e: Exception) {
            Logger.e("kakao sdk init fail, ${e}")
        }

        // get kakao sdk key hash
        val keyHash = Utility.getKeyHash(this)
        Logger.d("kakao sdk key hash: $keyHash")
    }
}
