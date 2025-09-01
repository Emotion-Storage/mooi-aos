package com.emotionstorage.auth.remote.dataSourceImpl

import com.emotionstorage.auth.data.dataSource.AuthRemoteDataSource
import com.emotionstorage.auth.data.model.SignupFormEntity
import com.emotionstorage.auth.remote.api.AuthApiService
import com.emotionstorage.auth.remote.modelMapper.SignupFormMapper
import com.emotionstorage.auth.remote.request.LoginRequestBody
import com.emotionstorage.common.DataResource
import com.emotionstorage.domain.model.User
import com.emotionstorage.remote.response.ResponseStatus
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRemoteDataSource {

    override suspend fun login(
        provider: User.AuthProvider,
        idToken: String
    ): DataResource<String> {
        try {
            val response = when (provider) {
                User.AuthProvider.KAKAO -> authApiService.postKakaoLogin(
                    LoginRequestBody(idToken)
                )

                User.AuthProvider.GOOGLE -> authApiService.postGoogleLogin(
                    LoginRequestBody(idToken)
                )
            }

            return if (response.status == ResponseStatus.Created.code) {
                response.data?.accessToken?.run {
                    DataResource.success(this)
                } ?: DataResource.error(Exception("No access token received"))
            } else DataResource.error(Exception(response.message))

        } catch (e: Exception) {
            return DataResource.Error(e)
        }
    }

    override suspend fun signup(
        provider: User.AuthProvider,
        signupFormEntity: SignupFormEntity
    ): DataResource<Boolean> {
        try {
            val response = when (provider) {
                User.AuthProvider.KAKAO -> authApiService.postKakaoSignup(
                    SignupFormMapper.toRemote(signupFormEntity)
                )

                User.AuthProvider.GOOGLE -> authApiService.postGoogleSignup(
                    SignupFormMapper.toRemote(signupFormEntity)
                )
            }

            return if (response.status == ResponseStatus.Created.code) DataResource.success(true)
            else DataResource.error(Exception(response.message))
        } catch (e: Exception) {
            return DataResource.error(e)
        }
    }

    override suspend fun checkSession(): Boolean {
        val response = authApiService.postSession()
        return response.status == ResponseStatus.OK.code
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