package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.repo.AuthRepository
import javax.inject.Inject

/**
 * Delete account use case
 * - delete account success: delete session info, delete user info, return true
 * - delete account fail: return false
 */
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
