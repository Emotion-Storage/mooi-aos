package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.AuthRepository
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import java.time.LocalDateTime
import javax.inject.Inject

class LoginUseCase @Inject
constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val fcmRepository: FcmRepository,
) {
    suspend operator fun invoke(provider: User.AuthProvider): DataState<String> =
        authRepository.login(provider).also {
            if (it is DataState.Success) {
                sessionRepository.saveSession(Session(it.data))
                fcmRepository.getToken()?.let {
                    fcmRepository.registerToken(it)
                }
                userRepository.getAccountInfo()
            }
            if (it is DataState.Error) {
                sessionRepository.deleteSession()
                userRepository.deleteUser()
                fcmRepository.deleteToken()
            }
        }
}
