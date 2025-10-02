package com.emotionstorage.auth.domain.usecase

import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AutomaticLoginUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val sessionRepository: SessionRepository,
        private val userRepository: UserRepository,
    ) {
        suspend operator fun invoke(): Flow<DataState<Boolean>> =
            authRepository.checkSession().map { it ->
                if (it is DataState.Error) {
                    sessionRepository.deleteSession()
                    userRepository.deleteUser()
                }
                it
            }
    }
