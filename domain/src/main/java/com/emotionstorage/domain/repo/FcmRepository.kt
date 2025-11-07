package com.emotionstorage.domain.repo

interface FcmRepository {
    suspend fun registerToken(token: String): Boolean

    suspend fun saveToken(token: String): Boolean

    suspend fun getToken(): String?

    suspend fun deleteToken(): Boolean
}
