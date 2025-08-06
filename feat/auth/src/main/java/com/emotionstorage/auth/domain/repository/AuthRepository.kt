package com.emotionstorage.auth.domain.repository

import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.common.DataResource
import com.emotionstorage.domain.model.User

interface AuthRepository {
    suspend fun login(provider: User.AuthProvider): DataResource<String>
    suspend fun loginWithIdToken(provider: User.AuthProvider, idToken: String): DataResource<String>
    suspend fun signup(provider: User.AuthProvider, signupForm: SignupForm): Boolean
    suspend fun checkSession(): Boolean
    suspend fun logout(): Boolean
    suspend fun deleteAccount(): Boolean
}