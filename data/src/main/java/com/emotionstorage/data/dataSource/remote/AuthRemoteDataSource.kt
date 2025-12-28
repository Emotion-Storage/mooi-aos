package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.data.model.SignupFormEntity
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User

interface AuthRemoteDataSource {
    /**
     * Login with id token
     * @return access token
     */
    suspend fun login(
        provider: User.AuthProvider,
        idToken: String,
    ): DataState<String>

    /**
     * Signup with id token
     */
    suspend fun signup(
        provider: User.AuthProvider,
        signupFormEntity: SignupFormEntity,
    ): DataState<Unit>

    /**
     * Check session
     * @return success
     */
    suspend fun checkSession(): Boolean

    suspend fun logout(): Boolean

    suspend fun deleteAccount(): Boolean
}
