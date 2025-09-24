package com.emotionstorage.domain.repo

import com.emotionstorage.domain.model.Session

interface SessionRepository {
    suspend fun getSession(): Session?
    suspend fun saveSession(session: Session): Boolean
    suspend fun deleteSession(): Boolean
}
