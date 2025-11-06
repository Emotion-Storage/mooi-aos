package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.repo.AuthRepository
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.di.ApplicationScope
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginWithIdTokenUseCase
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val handleLogin: HandleLoginUseCase,
    private val handleLogout: HandleLogoutUseCase,
) {
    suspend operator fun invoke(
        provider: User.AuthProvider,
        idToken: String,
    ): Flow<DataState<String>> =
        authRepository.loginWithIdToken(provider, idToken).onEach {
            if (it is DataState.Success) {
                handleLogin(it.data)
            }
            if (it is DataState.Error) {
                handleLogout()
            }
        }
}
