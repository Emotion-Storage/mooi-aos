package com.emotionstorage.auth.domain.usecase

import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.auth.domain.repository.AuthRepository
import com.emotionstorage.common.DataResource
import com.emotionstorage.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.sql.DataSource

/**
 * Signup use case
 * - signup success: return true
 * - signup fail: return false
 */
class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(provider: User.AuthProvider, signupForm: SignupForm): Flow<DataResource<Boolean>> = flow{
        emit(DataResource.loading(true))
        try {
            val result = authRepository.signup(provider, signupForm)
            emit(result)
        }catch (e: Exception){
            emit(DataResource.error(e))
        }finally {
            emit(DataResource.loading(false))
        }
    }
}