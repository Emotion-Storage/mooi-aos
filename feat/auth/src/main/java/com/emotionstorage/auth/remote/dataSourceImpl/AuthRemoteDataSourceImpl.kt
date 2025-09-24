package com.emotionstorage.auth.remote.dataSourceImpl

import com.emotionstorage.auth.data.dataSource.AuthRemoteDataSource
import com.emotionstorage.auth.data.model.SignupFormEntity
import com.emotionstorage.auth.remote.api.AuthApiService
import com.emotionstorage.auth.remote.modelMapper.GoogleSignupFormMapper
import com.emotionstorage.auth.remote.modelMapper.KakaoSignupFormMapper
import com.emotionstorage.auth.remote.request.GoogleLoginRequestBody
import com.emotionstorage.auth.remote.request.KakaoLoginRequestBody
import com.emotionstorage.domain.model.User
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
        ): String {
            try {
                // call login api
                val response =
                    when (provider) {
                        User.AuthProvider.KAKAO ->
                            authApiService.postKakaoLogin(
                                KakaoLoginRequestBody(idToken),
                            )

                        User.AuthProvider.GOOGLE ->
                            authApiService.postGoogleLogin(
                                GoogleLoginRequestBody(idToken),
                            )
                    }

                // return access token if success
                Logger.d("login response: $response")
                if (response.status == ResponseStatus.Created.code) {
                    response.data?.accessToken?.run {
                        return this
                    } ?: throw Exception("No access token received")
                } else {
                    throw Exception(response.code + "" + response.message)
                }
            } catch (e: Exception) {
                Logger.e("Login fail, $e")
                throw Exception("Login api failed", e)
            }
        }

        override suspend fun signup(
            provider: User.AuthProvider,
            signupFormEntity: SignupFormEntity,
        ): Boolean {
            try {
                val response =
                    when (provider) {
                        User.AuthProvider.KAKAO ->
                            authApiService.postKakaoSignup(
                                KakaoSignupFormMapper.toRemote(signupFormEntity),
                            )

                        User.AuthProvider.GOOGLE ->
                            authApiService.postGoogleSignup(
                                GoogleSignupFormMapper.toRemote(signupFormEntity),
                            )
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
