package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.SessionLocalDataSource
import com.emotionstorage.data.modelMapper.SessionMapper
import com.emotionstorage.domain.model.Session
import com.emotionstorage.domain.repo.SessionRepository
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionLocalDataSource: SessionLocalDataSource
) : SessionRepository {
    override suspend fun getSession(): Session? {
        return sessionLocalDataSource.getSession()?.run {
            SessionMapper.toDomain(this)
        }
    }

    override suspend fun saveSession(session: Session): Boolean {
        return sessionLocalDataSource.saveSession(SessionMapper.toData(session))
    }

    override suspend fun deleteSession(): Boolean {
        return sessionLocalDataSource.deleteSession()
    }
}