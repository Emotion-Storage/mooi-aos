package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.model.SignupForm
import com.emotionstorage.domain.repo.AuthRepository
import com.emotionstorage.domain.common.DataState
import javax.inject.Inject

/**
 * Signup use case
 * - signup success: return true
 * - signup fail: return false
 */
class SignupUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(signupForm: SignupForm): DataState<Unit> = authRepository.signup(signupForm)
    }
