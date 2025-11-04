package com.emotionstorage.local.dataSourceImpl

import com.emotionstorage.data.dataSource.local.SessionLocalDataSource
import com.emotionstorage.data.model.SessionEntity
import com.emotionstorage.local.modelMapper.SessionMapper
import com.emotionstorage.local.room.dao.SessionDao
import com.orhanobut.logger.Logger
import javax.inject.Inject

class SessionLocalDataSourceImpl
    @Inject
    constructor(
        private val sessionDao: SessionDao,
    ) : SessionLocalDataSource {
        override suspend fun saveSession(session: SessionEntity): Boolean =
            try {
                sessionDao.insertSession(SessionMapper.toLocal(session))
                true
            } catch (e: Exception) {
                Logger.e("saveSession error: $e")
                false
            }

        override suspend fun getSession(): SessionEntity? =
            try {
                sessionDao.getSession()?.let {
                    SessionMapper.toEntity(it)
                }
            } catch (e: Exception) {
                Logger.e("getSession error: $e")
                null
            }

        override suspend fun deleteSession(): Boolean =
            try {
                sessionDao.deleteSession()
                true
            } catch (e: Exception) {
                Logger.e("deleteSession error: $e")
                false
            }
    }
