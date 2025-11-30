package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.repo.AuthRepository
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

class LoginWithIdTokenUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val sessionRepository: SessionRepository,
        private val userRepository: UserRepository,
        private val fcmRepository: FcmRepository,
    ) {
        suspend operator fun invoke(
            provider: User.AuthProvider,
            idToken: String,
        ): DataState<String> =
            authRepository.loginWithIdToken(provider, idToken).also {
                if (it is DataState.Success) {
                    sessionRepository.saveSession(Session(it.data))
                    fcmRepository.getToken()?.let {
                        fcmRepository.registerToken(it)
                    }
                    userRepository.getAccountInfo()
                }
                if (it is DataState.Error) {
                    sessionRepository.deleteSession()
                    userRepository.deleteUser()
                    fcmRepository.deleteToken()
                }
            }
    }
