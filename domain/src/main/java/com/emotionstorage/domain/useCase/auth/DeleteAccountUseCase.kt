package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.repo.AuthRepository
import javax.inject.Inject

class DeleteAccountUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val handleLogout: HandleLogoutUseCase,
    ) {
        suspend operator fun invoke(): Boolean {
            val result = authRepository.deleteAccount()
            if (result) {
                handleLogout()
            }
            return result
        }
    }
