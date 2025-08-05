package com.emotionstorage.local.dataSourceImpl

import com.emotionstorage.data.dataSource.SessionLocalDataSource
import com.emotionstorage.data.model.SessionEntity
import com.emotionstorage.local.modelMapper.SessionMapper
import com.emotionstorage.local.room.dao.SessionDao
import javax.inject.Inject

class SessionLocalDataSourceImpl @Inject constructor(
    private val sessionDao: SessionDao
): SessionLocalDataSource{
    override suspend fun saveSession(session: SessionEntity): Boolean {
        return try {
            sessionDao.insertSession(SessionMapper.toLocal(session))
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getSession(): SessionEntity? {
        return try {
            sessionDao.getSession()?.let {
                SessionMapper.toEntity(it)
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteSession(): Boolean {
        return try {
            sessionDao.deleteSession()
            true
        } catch (e: Exception) {
            false
        }
    }

}