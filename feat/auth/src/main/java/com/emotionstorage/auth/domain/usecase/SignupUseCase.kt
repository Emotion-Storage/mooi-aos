package com.emotionstorage.auth.domain.usecase

import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Signup use case
 * - signup success: return true
 * - signup fail: return false
 */
class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        signupForm: SignupForm
    ): Flow<DataState<Boolean>> =
        authRepository.signup(signupForm)
}