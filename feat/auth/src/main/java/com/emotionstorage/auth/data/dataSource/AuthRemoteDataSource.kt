package com.emotionstorage.auth.data.dataSource

import com.emotionstorage.auth.data.model.SignupFormEntity
import com.emotionstorage.domain.model.User

// todo: Add provider model in data layer
interface AuthRemoteDataSource {
    /**
     * Login with id token
     * @return access token
     */
    suspend fun login(provider: User.AuthProvider, idToken: String): String

    /**
     * Signup with id token
     * @return success
     */
    suspend fun signup(provider: User.AuthProvider, signupFormEntity: SignupFormEntity): Boolean

    /**
     * Check session
     * @return success
     */
    suspend fun checkSession(): Boolean

    suspend fun logout(): Boolean
    suspend fun deleteAccount(): Boolean
}