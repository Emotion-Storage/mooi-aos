package com.emotionstorage.auth.ui.util

import android.app.Application
import com.emotionstorage.auth.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.orhanobut.logger.Logger

object KakaoSDKUtil {
    fun initApp(application: Application) {
        try {
            KakaoSdk.init(application, BuildConfig.KAKAO_NATIVE_APP_KEY)
            Logger.i("kakao sdk init success")
        }catch (e:Exception){
            Logger.e("kakao sdk init fail", e)
        }
    }
}