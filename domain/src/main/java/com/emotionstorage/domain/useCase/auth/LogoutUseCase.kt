package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.repo.AuthRepository
import javax.inject.Inject

/**
 *  Logout use case
 *  - logout success: delete session info, delete user info, return true
 *  - logout fail: return false
 */
class LogoutUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val handleLogout: HandleLogoutUseCase,
    ) {
        suspend operator fun invoke(): Boolean {
            val result = authRepository.logout()
            if (result) {
                handleLogout()
            }
            return result
        }
    }
