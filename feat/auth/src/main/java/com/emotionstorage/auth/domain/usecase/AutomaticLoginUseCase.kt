package com.emotionstorage.auth.domain.usecase

import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.common.DataResource
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Automatic login use case
 * - check if login session is expired using access token
 * - login success: return true
 * - login fail : delete session info, delete user info, return false
 */
class AutomaticLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository

){
    suspend operator fun invoke(): Flow<DataResource<Boolean>> = flow{
        emit(DataResource.loading(true))
        try {
            val result = authRepository.checkSession()
            // delete session & user info if auto login failed
            if (!result) {
                sessionRepository.deleteSession()
                userRepository.deleteUser()
            }
            // emit result
            emit(DataResource.success(result))

            /**
            // test use case
            delay(2000)
            emit(DataResource.success(false))
            **/
        }catch (e: Exception){
            emit(DataResource.error(e))
        }finally{
            emit(DataResource.loading(false))

        }
    }
}