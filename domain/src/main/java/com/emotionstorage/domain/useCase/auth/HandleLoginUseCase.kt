package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.di.ApplicationScope
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * - Login success logic use case
 * - Should only be **called in Auth Use cases**
 */
class HandleLoginUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val fcmRepository: FcmRepository,
    @ApplicationScope private val applicationScope: CoroutineScope,
) {
    // access token can be null when called on automatic login success
    operator fun invoke(accessToken: String? = null) {
        applicationScope.launch {
            // save session
            runCatching {
                accessToken?.let {
                    sessionRepository.saveSession(Session(it))
                    Napier.d("session saved")
                }
            }
            // todo: fetch & save user

            // save fcm token
            runCatching {
                fcmRepository.getToken()?.let {
                    fcmRepository.registerToken(it)
                    Napier.d("fcm token saved")
                }
            }
        }
    }
}
