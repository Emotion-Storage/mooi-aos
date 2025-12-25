package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject
constructor(
    private val authRepository: AuthRepository,
    private val handleLogin: HandleLoginUseCase,
    private val handleLogout: HandleLogoutUseCase

) {
    suspend operator fun invoke(provider: User.AuthProvider): DataState<String> {
        val loginResult = authRepository.login(provider)

        if (loginResult is DataState.Success) {
            val handleLoginResult = handleLogin(accessToken = loginResult.data)

            return if (handleLoginResult is DataState.Success) {
                loginResult
            } else {
                handleLoginResult
            }
        }

        if (loginResult is DataState.Error) {
            handleLogout()
        }
        return loginResult
    }
}
