package com.emotionstorage.auth.remote.dataSourceImpl

import com.emotionstorage.auth.data.dataSource.AuthRemoteDataSource
import com.emotionstorage.auth.data.model.SignupFormEntity
import com.emotionstorage.auth.remote.api.AuthApiService
import com.emotionstorage.auth.remote.modelMapper.SignupFormMapper
import com.emotionstorage.auth.remote.request.LoginRequestBody
import com.emotionstorage.domain.model.User
import com.emotionstorage.remote.response.ResponseStatus
import javax.inject.Inject

// todo: response status code & message 처리 세분화
class AuthRemoteDataSourceImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRemoteDataSource {

    // todo: change return type to contain boolean & access token
    override suspend fun login(
        provider: User.AuthProvider,
        idToken: String
    ): Boolean {
        val response = when (provider) {
            User.AuthProvider.KAKAO -> authApiService.postKakaoLogin(
                LoginRequestBody(idToken)
            )
            User.AuthProvider.GOOGLE -> authApiService.postGoogleLogin(
                LoginRequestBody(idToken)
            )
        }
        return response.status == ResponseStatus.Created
    }

    override suspend fun signup(
        provider: User.AuthProvider,
        signupFormEntity: SignupFormEntity
    ): Boolean {
        val response = when (provider) {
            User.AuthProvider.KAKAO -> authApiService.postKakaoSignup(
                SignupFormMapper.toRemote(signupFormEntity)
            )
            User.AuthProvider.GOOGLE -> authApiService.postGoogleSignup(
                SignupFormMapper.toRemote(signupFormEntity)
            )
        }
        return response.status == ResponseStatus.Created
    }

    override suspend fun checkSession(): Boolean {
        val response = authApiService.postSession()
        return response.status == ResponseStatus.OK
    }

    override suspend fun logout(): Boolean {
        val response = authApiService.postLogout()
        return response.status == ResponseStatus.OK
    }

    override suspend fun deleteAccount(): Boolean {
        val response = authApiService.deleteAccount()
        return response.status == ResponseStatus.OK
    }
}