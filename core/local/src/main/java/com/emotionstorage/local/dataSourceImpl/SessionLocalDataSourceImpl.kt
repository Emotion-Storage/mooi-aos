package com.emotionstorage.local.dataSourceImpl

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.emotionstorage.data.dataSource.local.SessionLocalDataSource
import com.emotionstorage.data.model.SessionEntity
import com.emotionstorage.local.modelMapper.SessionMapper
import com.emotionstorage.local.room.dao.SessionDao
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// todo: save access token in data store
class SessionLocalDataSourceImpl
    @Inject
    constructor(
        private val sessionDao: SessionDao,
        private val dataStore: DataStore<Preferences>,
    ) : SessionLocalDataSource {
        companion object {
            private val REFRESH_KEY = stringPreferencesKey("refresh_token")
        }

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
                dataStore.edit { preferences ->
                    preferences.remove(REFRESH_KEY)
                }
                true
            } catch (e: Exception) {
                Logger.e("deleteSession error: $e")
                false
            }

        override suspend fun saveRefreshToken(refreshToken: String): Boolean {
            dataStore.edit { preferences ->
                preferences[REFRESH_KEY] = refreshToken
            }
            return true
        }

        override suspend fun getRefreshToken(): String? =
            dataStore
                .data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }.map { preferences ->
                    preferences[REFRESH_KEY]
                }.firstOrNull()
    }
