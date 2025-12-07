package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.AuthRemoteDataSource
import com.emotionstorage.data.model.SignupFormEntity
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.User
import com.emotionstorage.remote.api.AuthApiService
import com.emotionstorage.remote.modelMapper.GoogleSignupFormMapper
import com.emotionstorage.remote.modelMapper.KakaoSignupFormMapper
import com.emotionstorage.remote.request.auth.GoogleLoginRequestBody
import com.emotionstorage.remote.request.auth.KakaoLoginRequestBody
import com.emotionstorage.remote.response.CustomHttpException
import com.emotionstorage.remote.response.ResponseStatus
import com.orhanobut.logger.Logger
import javax.inject.Inject

class AuthRemoteDataSourceImpl
    @Inject
    constructor(
        private val authApiService: AuthApiService,
    ) : AuthRemoteDataSource {
        override suspend fun login(
            provider: User.AuthProvider,
            idToken: String,
        ): DataState<String> =
            try {
                // call login api
                val response =
                    when (provider) {
                        User.AuthProvider.KAKAO -> {
                            authApiService.postKakaoLogin(
                                KakaoLoginRequestBody(idToken),
                            )
                        }

                        User.AuthProvider.GOOGLE -> {
                            authApiService.postGoogleLogin(
                                GoogleLoginRequestBody(idToken),
                            )
                        }
                    }

                response.data?.accessToken?.run {
                    DataState.Success(this)
                } ?: DataState.Error(Throwable("No access token received"))
            } catch (e: CustomHttpException) {
                Logger.e("Login http exception, $e")
                DataState.Error(e, ErrorCode.toErrorCode(e.code ?: ""), data = idToken)
            } catch (e: Exception) {
                Logger.e("Login exception, $e")
                DataState.Error(Throwable("Login api failed", e), data = idToken)
            }

        override suspend fun signup(
            provider: User.AuthProvider,
            signupFormEntity: SignupFormEntity,
        ): Boolean {
            try {
                val response =
                    when (provider) {
                        User.AuthProvider.KAKAO -> {
                            authApiService.postKakaoSignup(
                                KakaoSignupFormMapper.toRemote(signupFormEntity),
                            )
                        }

                        User.AuthProvider.GOOGLE -> {
                            authApiService.postGoogleSignup(
                                GoogleSignupFormMapper.toRemote(signupFormEntity),
                            )
                        }
                    }

                if (response.status == ResponseStatus.Created.code) {
                    return true
                } else {
                    throw Exception(response.code + "" + response.message)
                }
            } catch (e: Exception) {
                throw Exception("Signup api failed", e)
            }
        }

        override suspend fun checkSession(): Boolean {
            try {
                val response = authApiService.getAuthSession()
                if (response.status == ResponseStatus.OK.code) {
                    return true
                } else {
                    throw Exception(response.code + "" + response.message)
                }
            } catch (e: Exception) {
                throw Exception("Check session api failed", e)
            }
        }

        override suspend fun logout(): Boolean {
            val response = authApiService.postLogout()
            return response.status == ResponseStatus.OK.code
        }

        override suspend fun deleteAccount(): Boolean {
            val response = authApiService.deleteAccount()
            return response.status == ResponseStatus.OK.code
        }
    }
