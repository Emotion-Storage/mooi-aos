package com.emotionstorage.domain.useCase.fcm

import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import javax.inject.Inject

class SaveOrRegisterFcmTokenUseCase @Inject constructor(
    private val fcmRepository: FcmRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(fcmToken: String) {
        val session = sessionRepository.getSession()
        if (session != null) {
            // user is logged in, register fcm token
            fcmRepository.registerToken(fcmToken)
        } else {
            // user is not logged in, save fcm token to local
            fcmRepository.saveToken(fcmToken)
        }
    }
}
