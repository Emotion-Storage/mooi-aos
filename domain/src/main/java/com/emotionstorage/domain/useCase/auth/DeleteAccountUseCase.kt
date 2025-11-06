package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.di.ApplicationScope
import com.emotionstorage.domain.repo.AuthRepository
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
) {
    @Inject
    private lateinit var handleLogout: HandleLogoutUseCase

    suspend operator fun invoke(): Boolean {
        val result = authRepository.deleteAccount()
        if (result) {
            handleLogout()
        }
        return result
    }
}
