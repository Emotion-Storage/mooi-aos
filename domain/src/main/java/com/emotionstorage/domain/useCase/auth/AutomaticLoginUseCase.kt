package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AutomaticLoginUseCase
@Inject
constructor(
    private val authRepository: AuthRepository,
) {
    @Inject
    private lateinit var handleLogin: HandleLoginUseCase

    @Inject
    private lateinit var handleLogout: HandleLogoutUseCase


    suspend operator fun invoke(): Flow<DataState<Boolean>> =
        authRepository.checkSession().onEach { it ->
            if (it is DataState.Success) {
                handleLogin()
            }
            if (it is DataState.Error) {
                handleLogout()
            }
        }
}
