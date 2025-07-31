package com.emotionstorage.auth.domain.repository

import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.domain.model.User

interface AuthRepository {

    // todo: change return type to contain boolean & access token
    suspend fun login(provider: User.AuthProvider): Boolean
    suspend fun signup(provider: User.AuthProvider, signupForm: SignupForm): Boolean
    suspend fun checkSession(): Boolean
    suspend fun logout(): Boolean
    suspend fun deleteAccount(): Boolean
}