package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.remote.AuthRemoteDataSource
import com.emotionstorage.data.dataSource.remote.GoogleRemoteDataSource
import com.emotionstorage.data.dataSource.remote.KakaoRemoteDataSource
import com.emotionstorage.data.modelMapper.SignupFormMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.SignupForm
import com.emotionstorage.domain.model.User
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
                    User.AuthProvider.KAKAO -> kakaoRemoteDataSource.getIdToken()
                    User.AuthProvider.GOOGLE -> googleRemoteDataSource.getIdToken()
                }
            Napier.d("provider: $provider, idToken: ${idToken.substring(0..5) + "..."}")

            // login with id token
            try {
                authRemoteDataSource.login(provider, idToken)
            } catch (e: Exception) {
                DataState.Error(Throwable("failed to login with id token, $e"), data = idToken)
            }
        } catch (e: Exception) {
            DataState.Error(Throwable("failed to get social id token, $e"))
        }

    override suspend fun loginWithIdToken(
        provider: User.AuthProvider,
        idToken: String,
    ): Flow<DataState<String>> =
        flow {
            emit(DataState.Loading(true))
            try {
                authRemoteDataSource.login(provider, idToken).handle(
                    onSuccess = {
                        emit(DataState.Success(it))
                    },
                    onError = { throwable, code, data ->
                        emit(DataState.Error(throwable, code, data))
                    },
                )
            } catch (e: Exception) {
                emit(DataState.Error(throwable = e))
            } finally {
                emit(DataState.Loading(false))
            }
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

    override suspend fun checkSession(): Flow<DataState<Boolean>> =
        flow {
            emit(DataState.Loading(true))
            try {
                emit(DataState.Success(authRemoteDataSource.checkSession()))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(false))
            }
        }

    override suspend fun logout(): Boolean = authRemoteDataSource.logout()

    override suspend fun deleteAccount(): Boolean = authRemoteDataSource.deleteAccount()
}
