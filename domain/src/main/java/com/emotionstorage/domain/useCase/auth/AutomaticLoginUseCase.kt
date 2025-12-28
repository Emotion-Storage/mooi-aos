package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.AuthRepository
import com.emotionstorage.domain.repo.FcmRepository
import javax.inject.Inject

class AutomaticLoginUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val fcmRepository: FcmRepository,
        private val handleLogout: HandleLogoutUseCase,
    ) {
        suspend operator fun invoke(): DataState<Boolean> {
            val checkSessionResult = authRepository.checkSession()

            if (checkSessionResult is DataState.Success) {
                runCatching {
                    fcmRepository.getToken()?.let {
                        fcmRepository.registerToken(it)
                    }
                }
            }
            if (checkSessionResult is DataState.Error) {
                handleLogout()
            }
            return checkSessionResult
        }
    }
