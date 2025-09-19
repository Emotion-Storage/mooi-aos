package com.emotionstorage.domain.repo

import com.emotionstorage.domain.model.User
import kotlinx.coroutines.flow.Flow
import com.emotionstorage.domain.common.DataState

interface UserRepository {
    suspend fun saveUser(user:User): Boolean
    suspend fun getUser(): Flow<DataState<User>>
    suspend fun deleteUser(): Boolean
}