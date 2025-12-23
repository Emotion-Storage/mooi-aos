package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.AuthRepository
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import io.github.aakira.napier.Napier
import javax.inject.Inject

class LoginUseCase @Inject
constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val fcmRepository: FcmRepository,
) {
    suspend operator fun invoke(provider: User.AuthProvider): DataState<String> {
        val loginResult = authRepository.login(provider)

        // todo: handle login success handling logic
        if (loginResult is DataState.Success) {
            sessionRepository.saveSession(Session(loginResult.data))
            try {
                fcmRepository.getToken()?.let {
                    fcmRepository.registerToken(it)
                }
            }catch(e: Exception){
                Napier.e("fcm token get/register error", e)
            }
            userRepository.getAccountInfo()
        }

        // todo: handle login error handling logic
        if (loginResult is DataState.Error) {
            sessionRepository.deleteSession()
            userRepository.deleteUser()
            try {
                fcmRepository.deleteToken()
            } catch (e: Exception){
                Napier.e("fcm token delete error", e)
            }
        }

        return loginResult
    }

}
