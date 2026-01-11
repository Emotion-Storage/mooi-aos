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
import java.time.LocalDateTime
import javax.inject.Inject

class TimeCapsuleLocalDataSourceImpl @Inject constructor(
    private val timeCapsuleDao: TimeCapsuleDao,
) : TimeCapsuleLocalDataSource {

    override suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean {
        try {
            timeCapsuleDao.upsertAll(timeCapsules.map { TimeCapsuleMapper.toLocal(it) })
            return true
        } catch (e: Exception) {
            Logger.e("saveTimeCapsules error: $e")
            return false
        }
    }

    override fun getPagingSource(
        status: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): PagingSource<Int, TimeCapsuleEntity> {
        Logger.d("getPagingSource called - status: $status, startDate: $startDate, endDate: $endDate")
        return timeCapsuleDao.pagingSource(
            status = status,
            startDate = startDate,
            endDate = endDate,
        ).mapValue {
            TimeCapsuleMapper.toData(it)
        }
    }

    override suspend fun clearAll(): Boolean {
        try {
            timeCapsuleDao.clearAll()
            return true
        } catch (e: Exception) {
            Logger.e("clearAll error: $e")
            return false
        }
    }
}
