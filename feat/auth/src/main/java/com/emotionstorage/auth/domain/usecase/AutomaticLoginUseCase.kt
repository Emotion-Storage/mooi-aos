package com.emotionstorage.auth.domain.usecase

import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

/**
 * Automatic login use case
 * - check if login session is expired using access token
 * - login success: return true
 * - login fail : delete session info, delete user info, return false
 */
class AutomaticLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository

){
    suspend operator fun invoke(): Boolean{
        val result = authRepository.checkSession()
        if(!result){
            sessionRepository.deleteSession()
            userRepository.deleteUser()
        }
        return result
    }
}