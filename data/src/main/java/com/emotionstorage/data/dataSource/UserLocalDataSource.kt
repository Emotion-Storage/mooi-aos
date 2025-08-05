package com.emotionstorage.data.dataSource

import com.emotionstorage.data.model.UserEntity

interface UserLocalDataSource {
    suspend fun saveUser(user: UserEntity): Boolean
    suspend fun getUser(): UserEntity?
    suspend fun deleteUser(): Boolean
}