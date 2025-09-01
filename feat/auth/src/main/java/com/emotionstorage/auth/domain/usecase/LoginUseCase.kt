package com.emotionstorage.auth.domain.usecase

import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.common.DataResource
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Login use case
 * - login success: save session info, return true
 * - login fail: delete session info, delete user info, return false
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(provider: User.AuthProvider): Flow<DataResource<Boolean>> = flow {
        emit(DataResource.loading(true))

        try {
            val result = authRepository.login(provider)
            if (result is DataResource.Success) {
                sessionRepository.saveSession(Session(result.data))
                emit(DataResource.success(true))
            }
            if (result is DataResource.Error) {
                throw result.throwable
            }
        } catch (e: Exception) {
            sessionRepository.deleteSession()
            userRepository.deleteUser()
            emit(DataResource.error(e))
        } finally {
            emit(DataResource.loading(false))
        }
    }
}