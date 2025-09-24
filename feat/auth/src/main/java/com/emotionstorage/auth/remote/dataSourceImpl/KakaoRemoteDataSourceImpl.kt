package com.emotionstorage.auth.remote.dataSourceImpl

import android.content.Context
import com.emotionstorage.auth.data.dataSource.KakaoRemoteDataSource
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.orhanobut.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class KakaoRemoteDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : KakaoRemoteDataSource {

    override suspend fun getIdToken(): String = suspendCancellableCoroutine { continuation ->
        fun handleToken(token: OAuthToken) {
            Logger.i("Kakao login success: $token")
            continuation.resume(value = token.accessToken) {
                // cancel callback
            }
        }

        fun handleError(error: Throwable?) {
            Logger.e("Kakao login error: $error")
            continuation.resumeWithException(error ?: Exception("Unknown Kakao login error"))
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        Logger.d("KakaoTalk login cancelled by user")
                        continuation.resumeWithException(error)
                    } else {
                        Logger.d("KakaoTalk login failed, fallback to account login")
                        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                            if (error != null) {
                                handleError(error)
                            } else if (token != null) handleToken(token)
                        }
                    }
                } else if (token != null) {
                    handleToken(token)
                }
            }
        } else {
            Logger.d("KakaoTalk not available, trying Kakao account login")
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    handleError(error)
                } else if (token != null) handleToken(token)
            }
        }
    }
}
