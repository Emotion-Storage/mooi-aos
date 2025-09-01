package com.emotionstorage.auth.data.repoImpl

import com.emotionstorage.auth.data.dataSource.AuthRemoteDataSource
import com.emotionstorage.auth.data.dataSource.GoogleRemoteDataSource
import com.emotionstorage.auth.data.dataSource.KakaoRemoteDataSource
import com.emotionstorage.auth.data.modelMapper.SignupFormMapper
import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.common.DataResource
import com.emotionstorage.domain.model.User
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val kakaoRemoteDataSource: KakaoRemoteDataSource,
    private val googleRemoteDataSource: GoogleRemoteDataSource
) : AuthRepository {

    override suspend fun login(provider: User.AuthProvider): DataResource<String> {
        try {
            // get id token from providers
            val tokenResult = when (provider) {
                User.AuthProvider.KAKAO -> kakaoRemoteDataSource.getIdToken()
                User.AuthProvider.GOOGLE -> googleRemoteDataSource.getIdToken()
            }

            // login with id token
            return if (tokenResult is DataResource.Success) {
                authRemoteDataSource.login(provider, tokenResult.data)
            } else tokenResult
        } catch (e: Exception) {
            return DataResource.error(e)
        }
    }

    override suspend fun signup(
        provider: User.AuthProvider,
        signupForm: SignupForm
    ): DataResource<Boolean> {
        try {
            return authRemoteDataSource.signup(provider, SignupFormMapper.toData(signupForm))
        } catch (e: Exception) {
            return DataResource.error(e)
        }
    }

    override suspend fun checkSession(): Boolean {
        return authRemoteDataSource.checkSession()
    }

    override suspend fun logout(): Boolean {
        return authRemoteDataSource.logout()
    }

    override suspend fun deleteAccount(): Boolean {
        return authRemoteDataSource.deleteAccount()
    }
}