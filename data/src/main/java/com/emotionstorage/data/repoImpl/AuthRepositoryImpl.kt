package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.local.SessionLocalDataSource
import com.emotionstorage.data.dataSource.remote.AuthRemoteDataSource
import com.emotionstorage.data.dataSource.remote.KakaoRemoteDataSource
import com.emotionstorage.data.dataSource.remote.ReissueRemoteDataSource
import com.emotionstorage.data.model.SessionEntity
import com.emotionstorage.data.modelMapper.SignupFormMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.SignupForm
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.domain.repo.AuthRepository
import io.github.aakira.napier.Napier
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val reissueRemoteDataSource: ReissueRemoteDataSource,
    private val kakaoRemoteDataSource: KakaoRemoteDataSource,
) : AuthRepository {
    override suspend fun googleLogin(idToken: String): DataState<String> {
        // login with id token
        return try {
            loginWithIdToken(AuthProvider.GOOGLE, idToken)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun kakaoLogin(): DataState<String> {
        var idToken: String? = null

        // get id token from provider
        try {
            idToken = kakaoRemoteDataSource.getIdToken()
            Napier.d("Get kakao id token success")
        } catch (e: Exception) {
            return DataState.Error(
                throwable = e,
                code = ErrorCode.CLIENT_ID_TOKEN_ISSUE_FAIL,
            )
        }
        // login with id token
        return try {
            loginWithIdToken(AuthProvider.KAKAO, idToken)
        } catch (e: Exception) {
            DataState.Error(e)
        }
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

    override suspend fun signup(signupForm: SignupForm): DataState<Unit> =
        try {
            if (signupForm.provider == null) throw Exception("provider is null")
            if (signupForm.idToken == null) throw Exception("idToken is null")

            authRemoteDataSource.signup(
                signupForm.provider!!,
                SignupFormMapper.toData(signupForm),
            )
        } catch (e: Exception) {
            DataState.Error(e)
        }

    override suspend fun checkSession(): DataState<Unit> {
        try {
            val result = authRemoteDataSource.checkSession()
            if (result !is DataState.Error) {
                return result
            }
            if (result.code != ErrorCode.ACCESS_TOKEN_EXPIRED && result.code != ErrorCode.USER_NOT_FOUND) {
                return result
            }

            // refresh access token
            val reissueResult = reissueRemoteDataSource.reissueAccessToken()
            if (reissueResult !is DataState.Success) {
                return DataState.Error(
                    Throwable("failed to reissue access token"),
                    if (reissueResult is DataState.Error) {
                        reissueResult.code
                    } else {
                        ErrorCode.UNKNOWN
                    },
                )
            }
            // save new access token & retry check session
            return if (sessionLocalDataSource.saveSession(SessionEntity(reissueResult.data))) {
                authRemoteDataSource.checkSession()
            } else {
                DataState.Error(Throwable("failed to save new access token"))
            }
        } catch (e: Exception) {
            return DataState.Error(e)
        }
    }

    override suspend fun logout(): Boolean = authRemoteDataSource.logout()

    override suspend fun deleteAccount(): Boolean = authRemoteDataSource.deleteAccount()
}
