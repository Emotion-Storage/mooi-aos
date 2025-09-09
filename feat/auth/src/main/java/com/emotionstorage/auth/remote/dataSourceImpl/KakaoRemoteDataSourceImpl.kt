package com.emotionstorage.auth.remote.dataSourceImpl

import android.content.Context
import com.emotionstorage.auth.data.dataSource.KakaoRemoteDataSource
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.orhanobut.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class KakaoRemoteDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : KakaoRemoteDataSource {

    override suspend fun getIdToken(): String {
        val idTokenFlow: Flow<String?> = channelFlow {
            val sendToken: (OAuthToken?) -> Unit = { token ->
                Logger.i("Kakao login success: ${token?.accessToken}")
                trySend(token?.idToken)
                close()
            }

            val sendError: (Throwable?) -> Unit = { error ->
                Logger.e("Kakao login error: ${error.toString()}")
                trySend(null)
                close()
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            sendError(error)
                            return@loginWithKakaoTalk
                        }

                        // fallback to account
                        Logger.d("KakaoTalk error, login with account...")
                        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                            if (error != null) {
                                sendError(error)
                            } else {
                                sendToken(token)
                            }
                        }
                    } else {
                        sendToken(token)
                    }
                }
            } else {
                Logger.d("KakaoTalk not available, login with account...")
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    if (error != null) {
                        sendError(error)
                    } else {
                        sendToken(token)
                    }
                }
            }
        }

        return idTokenFlow.first() ?: throw Exception("Kakao login failed: no id token")
    }
}