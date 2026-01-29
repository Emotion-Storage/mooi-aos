package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import io.github.aakira.napier.Napier
import javax.inject.Inject

class HandleLoginUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val fcmRepository: FcmRepository,
) {
    suspend operator fun invoke(accessToken: String): DataState<String> {
        try {
            fcmRepository.getToken()?.let {
                fcmRepository.registerToken(it)
            }
        } catch (e: Exception) {
            // ignore fcm token register error for now
            Napier.e("fcm token get/register error", e)
        }

        try {
            if (sessionRepository.saveSession(Session(accessToken)) && userRepository.getAndSaveUser()) {
                return DataState.Success(accessToken)
            } else {
                throw Exception("session/user save error")
            }
        } catch (e: Exception) {
            Napier.e("handle login client error", e)
            return DataState.Error(e, ErrorCode.CLIENT_LOGIN_ERROR, accessToken)
        }
    }
}
