package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: User): Boolean

    suspend fun getUser(): Flow<DataState<User>>

    suspend fun deleteUser(): Boolean
}
