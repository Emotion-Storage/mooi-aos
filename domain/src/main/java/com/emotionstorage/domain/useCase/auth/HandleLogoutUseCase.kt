package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

class HandleLogoutUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val fcmRepository: FcmRepository,
) {
    suspend operator fun invoke(): DataState<Unit> {
        // ignore any client error on logout for now
        runCatching {
            sessionRepository.deleteSession()
        }
        runCatching {
            userRepository.deleteUser()
        }
        runCatching {
            fcmRepository.deleteToken()
        }
        return DataState.Success(Unit)
    }
}
