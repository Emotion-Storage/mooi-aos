package com.emotionstorage.data.dataSource.local

import com.emotionstorage.data.model.UserEntity

interface UserLocalDataSource {
    suspend fun saveUser(user: UserEntity): Boolean

    suspend fun getUser(): UserEntity?

    suspend fun deleteUser(): Boolean

    suspend fun updateUserNickname(nickname: String)
}
