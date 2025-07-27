package com.emotionstorage.domain.repo

import com.emotionstorage.domain.model.User

interface UserRepository {
    suspend fun saveUser(user:User): Boolean
    suspend fun getUser(): User?
    suspend fun deleteUser(): Boolean
}