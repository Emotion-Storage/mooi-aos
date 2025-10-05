package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.repo.AuthRepository
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginWithIdTokenUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val sessionRepository: SessionRepository,
        private val userRepository: UserRepository,
    ) {
        suspend operator fun invoke(
            provider: User.AuthProvider,
            idToken: String,
        ): Flow<DataState<String>> =
            authRepository.loginWithIdToken(provider, idToken).map {
                if (it is DataState.Success) {
                    sessionRepository.saveSession(Session(it.data))
                }
                if (it is DataState.Error) {
                    sessionRepository.deleteSession()
                    userRepository.deleteUser()
                }
                it
            }
    }
