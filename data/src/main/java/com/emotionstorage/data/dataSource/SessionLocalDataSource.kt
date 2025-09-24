package com.emotionstorage.data.dataSource

import com.emotionstorage.data.model.SessionEntity

interface SessionLocalDataSource {
    suspend fun saveSession(session: SessionEntity): Boolean
    suspend fun getSession(): SessionEntity?
    suspend fun deleteSession(): Boolean
}
