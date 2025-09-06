package com.emotionstorage.auth.remote.dataSourceImpl

import android.content.Context
import com.emotionstorage.auth.data.dataSource.KakaoRemoteDataSource
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.orhanobut.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class KakaoRemoteDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : KakaoRemoteDataSource {

    override suspend fun getIdToken(): String {
        var idTokenFlow: Flow<String?> = flow {
            val loginCallback: (OAuthToken?, Throwable?, errorCallback: () -> Unit) -> Unit =
                { token, error, errorCallback ->
                    CoroutineScope(Dispatchers.IO).launch(CoroutineName("Kakao Login")) {
                        if (error != null) {
                            Logger.e("kakao login error ", error)
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                emit(null)
                            }
                            errorCallback()
                        } else if (token != null) {
                            Logger.i("kakao login success ${token.accessToken}")
                            emit(token.idToken)
                        }
                    }
                }

            val loginWithAccountCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                CoroutineScope(Dispatchers.IO).launch(CoroutineName("Kakao Account Login")) {
                    if (error != null) {
                        Logger.e("kakao login with account error", error)
                        emit(null)
                    } else if (token != null) {
                        Logger.i("kakao login with account success")
                        emit(token.idToken)
                    }
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    loginCallback(token, error) {
                        // login with account when login fail
                        UserApiClient.instance.loginWithKakaoAccount(
                            context,
                            callback = loginWithAccountCallback
                        )
                    }
                }
            } else {
                // login with account when login unavailable
                UserApiClient.instance.loginWithKakaoAccount(
                    context,
                    callback = loginWithAccountCallback
                )
            }
        }

        val idToken = CoroutineScope(Dispatchers.IO).async(CoroutineName("Kakao Login")) {
            return@async idTokenFlow.first() ?: throw Exception("kakao login fail, no id token")
        }.await()
        return idToken
    }
}