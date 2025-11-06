package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.di.ApplicationScope
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * - Logout success logic use case
 * - Should only be **called in Auth Use cases**
 */
class HandleLogoutUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val fcmRepository: FcmRepository,
    @ApplicationScope private val applicationScope: CoroutineScope,
) {
    operator fun invoke() {
        applicationScope.launch {
            runCatching {
                sessionRepository.deleteSession()
            }
            runCatching {
                userRepository.deleteUser()
            }
            runCatching {
                fcmRepository.deleteToken()
            }
        }
    }
}
