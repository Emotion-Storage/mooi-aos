package com.emotionstorage.data.dataSource.local

import com.emotionstorage.data.model.SessionEntity

interface SessionLocalDataSource {
    suspend fun saveSession(session: SessionEntity): Boolean

    suspend fun getSession(): SessionEntity?

    suspend fun deleteSession(): Boolean

    suspend fun saveRefreshToken(refreshToken: String): Boolean

    suspend fun getRefreshToken(): String?
}
