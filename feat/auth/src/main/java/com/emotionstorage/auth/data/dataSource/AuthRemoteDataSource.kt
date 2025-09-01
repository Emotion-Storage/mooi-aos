package com.emotionstorage.auth.data.dataSource

import com.emotionstorage.auth.data.model.SignupFormEntity
import com.emotionstorage.common.DataResource
import com.emotionstorage.domain.model.User

// todo: Add provider model in data layer
interface AuthRemoteDataSource {
    suspend fun login(provider: User.AuthProvider, idToken: String): DataResource<String>
    suspend fun signup(provider: User.AuthProvider, signupFormEntity: SignupFormEntity): DataResource<Boolean>
    suspend fun checkSession(): Boolean
    suspend fun logout(): Boolean
    suspend fun deleteAccount(): Boolean
}