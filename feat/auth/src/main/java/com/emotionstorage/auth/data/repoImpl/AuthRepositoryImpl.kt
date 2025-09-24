package com.emotionstorage.auth.data.repoImpl

import com.emotionstorage.auth.data.dataSource.AuthRemoteDataSource
import com.emotionstorage.auth.data.dataSource.GoogleRemoteDataSource
import com.emotionstorage.auth.data.dataSource.KakaoRemoteDataSource
import com.emotionstorage.auth.data.modelMapper.SignupFormMapper
import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authRemoteDataSource: AuthRemoteDataSource,
        private val kakaoRemoteDataSource: KakaoRemoteDataSource,
        private val googleRemoteDataSource: GoogleRemoteDataSource,
    ) : AuthRepository {
        override suspend fun login(provider: User.AuthProvider): Flow<DataState<String>> =
            flow {
                emit(DataState.Loading(true))

                try {
                    // get id token from providers
                    val idToken =
                        when (provider) {
                            User.AuthProvider.KAKAO -> kakaoRemoteDataSource.getIdToken()
                            User.AuthProvider.GOOGLE -> googleRemoteDataSource.getIdToken()
                        }
                    Logger.d("provider: $provider, idToken: $idToken")

                    // login with id token
                    try {
                        val accessToken = authRemoteDataSource.login(provider, idToken)
                        emit(DataState.Success(accessToken))
                    } catch (e: Exception) {
                        emit(DataState.Error(throwable = e, data = idToken))
                    }
                } catch (e: Exception) {
                    emit(DataState.Error(e))
                } finally {
                    emit(DataState.Loading(false))
                }
            }

        override suspend fun loginWithIdToken(
            provider: User.AuthProvider,
            idToken: String,
        ): Flow<DataState<String>> =
            flow {
                emit(DataState.Loading(true))
                try {
                    val accessToken = authRemoteDataSource.login(provider, idToken)
                    emit(DataState.Success(accessToken))
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

                    authRemoteDataSource.signup(signupForm.provider, SignupFormMapper.toData(signupForm))
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
