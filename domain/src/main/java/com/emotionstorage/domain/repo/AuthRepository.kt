package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.SignupForm
import com.emotionstorage.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(provider: User.AuthProvider): DataState<String>

    suspend fun loginWithIdToken(
        provider: User.AuthProvider,
        idToken: String,
    ): DataState<String>

    suspend fun signup(signupForm: SignupForm): DataState<Unit>

    suspend fun checkSession(): DataState<Boolean>

    suspend fun logout(): Boolean

    suspend fun deleteAccount(): Boolean
}
