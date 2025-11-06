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

class LoginUseCase
@Inject
constructor(
    private val authRepository: AuthRepository,
) {
    @Inject
    private lateinit var handleLogin: HandleLoginUseCase

    @Inject
    private lateinit var handleLogout: HandleLogoutUseCase

    suspend operator fun invoke(provider: User.AuthProvider): Flow<DataState<String>> =
        authRepository.login(provider).onEach {
            if (it is DataState.Success) {
                handleLogin(it.data)
            }
            if (it is DataState.Error) {
                handleLogout()
            }
        }
}
