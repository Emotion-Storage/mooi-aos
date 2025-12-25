package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.remote.AuthRemoteDataSource
import com.emotionstorage.data.dataSource.remote.GoogleRemoteDataSource
import com.emotionstorage.data.dataSource.remote.KakaoRemoteDataSource
import com.emotionstorage.data.modelMapper.SignupFormMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.SignupForm
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.domain.repo.AuthRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val kakaoRemoteDataSource: KakaoRemoteDataSource,
    private val googleRemoteDataSource: GoogleRemoteDataSource,
) : AuthRepository {
    override suspend fun login(provider: User.AuthProvider): DataState<String> =
        try {
            // get id token from providers
            val idToken =
                when (provider) {
                    AuthProvider.KAKAO -> kakaoRemoteDataSource.getIdToken()
                    AuthProvider.GOOGLE -> googleRemoteDataSource.getIdToken()
                }
            Napier.d("GetIdToken success, provider: $provider, idToken: ${idToken.take(6) + "..."}")

            loginWithIdToken(provider, idToken)
        } catch (e: Exception) {
            DataState.Error(
                e,
                when (provider) {
                    AuthProvider.GOOGLE -> ErrorCode.INVALID_ID_TOKEN
                    AuthProvider.KAKAO -> ErrorCode.INVALID_KAKAO_ACCESS_TOKEN
                },
            )
        }

    override suspend fun loginWithIdToken(
        provider: AuthProvider,
        idToken: String,
    ): DataState<String> =
        try {
            authRemoteDataSource.login(provider, idToken)
        } catch (e: Exception) {
            DataState.Error(Throwable("failed to login with id token, $e"), data = idToken)
        }

    override suspend fun signup(signupForm: SignupForm): Flow<DataState<Boolean>> =
        flow {
            emit(DataState.Loading(true))
            try {
                if (signupForm.provider == null) throw Exception("provider is null")
                if (signupForm.idToken == null) throw Exception("idToken is null")

                authRemoteDataSource.signup(
                    signupForm.provider!!,
                    SignupFormMapper.toData(signupForm),
                )
                emit(DataState.Success(true))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(false))
            }
        }

    override suspend fun checkSession(): DataState<Boolean> =
        try {
            DataState.Success(authRemoteDataSource.checkSession())
        } catch (e: Exception) {
            DataState.Error(e)
        }


    override suspend fun logout(): Boolean = authRemoteDataSource.logout()

    override suspend fun deleteAccount(): Boolean = authRemoteDataSource.deleteAccount()
}
