package com.emotionstorage.data.dataSource.local

interface FcmLocalDataSource {
    suspend fun saveToken(token: String): Boolean
    suspend fun getToken(): String?
}
