package com.emotionstorage.data.dataSource

import com.emotionstorage.data.model.UserEntity

interface UserLocalDataSource {
    suspend fun getUser(): UserEntity?
}