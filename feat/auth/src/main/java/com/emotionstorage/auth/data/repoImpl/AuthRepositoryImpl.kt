package com.emotionstorage.auth.data.repoImpl

import com.emotionstorage.auth.data.dataSource.AuthRemoteDataSource
import com.emotionstorage.auth.data.dataSource.GoogleRemoteDataSource
import com.emotionstorage.auth.data.dataSource.KakaoRemoteDataSource
import com.emotionstorage.auth.data.modelMapper.SignupFormMapper
import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.domain.model.User
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val kakaoRemoteDataSource: KakaoRemoteDataSource,
    private val googleRemoteDataSource: GoogleRemoteDataSource
) : AuthRepository {

    // todo: change return type to contain boolean & access token
    override suspend fun login(provider: User.AuthProvider): Boolean {
        val idToken = when(provider){
            User.AuthProvider.KAKAO -> kakaoRemoteDataSource.getIdToken()
            User.AuthProvider.GOOGLE -> googleRemoteDataSource.getIdToken()
        }
        return authRemoteDataSource.login(provider, idToken)
    }

    override suspend fun signup(
        provider: User.AuthProvider,
        signupForm: SignupForm
    ): Boolean {
        return authRemoteDataSource.signup(provider, SignupFormMapper.toData(signupForm))
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