package com.emotionstorage.auth.domain.usecase

import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.common.DataResource
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

/**
 * Login with id token use case
 * - login success: save session info, return true
 * - login fail: delete session info, delete user info, return false
 */
class LoginWithIdTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(provider: User.AuthProvider, idToken: String): Boolean {
        val result = authRepository.loginWithIdToken(provider, idToken)
        when(result){
            is DataResource.Success -> {
                sessionRepository.saveSession(
                    Session(accessToken = result.data)
                )
                return true
            }
            is DataResource.Error -> {
                sessionRepository.deleteSession()
                userRepository.deleteUser()
                return false
            }
            else -> return false
        }
    }
}