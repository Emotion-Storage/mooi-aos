package com.emotionstorage.auth.domain.usecase

import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.domain.model.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(provider: User.AuthProvider): Boolean{
        // todo: get access token from return value of repository's login function
        // todo: get & save user info using access token
        return authRepository.login(provider)
    }
}