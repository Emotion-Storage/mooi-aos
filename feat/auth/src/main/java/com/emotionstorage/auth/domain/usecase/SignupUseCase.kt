package com.emotionstorage.auth.domain.usecase

import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.domain.model.User
import javax.inject.Inject

/**
 * Signup use case
 * - signup success: return true
 * - signup fail: return false
 */
class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(provider: User.AuthProvider, signupForm: SignupForm): Boolean{
        return authRepository.signup(provider, signupForm)
    }
}