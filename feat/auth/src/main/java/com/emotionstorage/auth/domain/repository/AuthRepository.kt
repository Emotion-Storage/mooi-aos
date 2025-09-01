package com.emotionstorage.auth.domain.repository

import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 *
 */
interface AuthRepository {
    suspend fun login(provider: User.AuthProvider): Flow<DataState<String>>
    suspend fun loginWithIdToken(provider: User.AuthProvider, idToken: String): Flow<DataState<String>>
    suspend fun signup(signupForm: SignupForm): Flow<DataState<Boolean>>
    suspend fun checkSession(): Flow<DataState<Boolean>>
    suspend fun logout(): Boolean
    suspend fun deleteAccount(): Boolean
}