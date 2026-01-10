package com.emotionstorage.local.dataSourceImpl

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.paging.PagingSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.local.modelMapper.TimeCapsuleMapper
import com.emotionstorage.local.room.dao.TimeCapsuleDao
import com.emotionstorage.local.util.mapValue
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimeCapsuleLocalDataSourceImpl @Inject constructor(
    private val timeCapsuleDao: TimeCapsuleDao,
    private val dataStore: DataStore<Preferences>,
) : TimeCapsuleLocalDataSource {
    companion object {
        private val KEY = longPreferencesKey("time_capsule_local_last_updated")
    }

    private suspend fun updateLastUpdated() {
        dataStore.edit { it[KEY] = System.currentTimeMillis() }
    }

    override suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean {
        try {
            timeCapsuleDao.upsertAll(timeCapsules.map { TimeCapsuleMapper.toLocal(it) })
            updateLastUpdated()
            return true
        } catch (e: Exception) {
            Logger.e("saveTimeCapsules error: $e")
            return false
        }
    }

    override fun getPagingSource(): PagingSource<Int, TimeCapsuleEntity> {
        Logger.d("getPagingSource called")
        return timeCapsuleDao.pagingSource().mapValue {
            TimeCapsuleMapper.toData(it)
        }
    }

    override suspend fun lastUpdated(): Flow<Long?> =
        dataStore.data.catch { e ->
            if (e is IOException) null
            else throw e
        }.map { it[KEY] }


    override suspend fun clearAll(): Boolean {
        try {
            timeCapsuleDao.clearAll()
            updateLastUpdated()
            return true
        } catch (e: Exception) {
            Logger.e("clearAll error: $e")
            return false
        }
    }
}
