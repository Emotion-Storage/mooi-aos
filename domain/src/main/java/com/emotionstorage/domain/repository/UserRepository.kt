package com.emotionstorage.domain.repository

import com.emotionstorage.domain.model.User

interface UserRepository {
    suspend fun getUser(): User
}