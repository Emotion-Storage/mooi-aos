package com.emotionstorage.auth.domain.usecase

import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

/**
 *  Logout use case
 *  - logout success: delete session info, delete user info, return true
 *  - logout fail: return false
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): Boolean{
        val result = authRepository.logout()
        if(result){
            sessionRepository.deleteSession()
            userRepository.deleteUser()
        }
        return result
    }
}