package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.di.ApplicationScope
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class HandleLoginUseCase(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val fcmRepository: FcmRepository,
    @ApplicationScope private val applicationScope: CoroutineScope,
) {
    operator fun invoke(accessToken: String? = null) {
        applicationScope.launch {
            // save session
            runCatching {
                accessToken?.let{
                    sessionRepository.saveSession(Session(it))
                }
            }
            // todo: fetch & save user

            // save fcm token
            runCatching {
                fcmRepository.getToken()?.let {
                    fcmRepository.registerToken(it)
                }
            }
        }
    }
}
