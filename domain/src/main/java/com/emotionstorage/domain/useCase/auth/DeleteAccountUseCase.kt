package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.di.ApplicationScope
import com.emotionstorage.domain.repo.AuthRepository
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
