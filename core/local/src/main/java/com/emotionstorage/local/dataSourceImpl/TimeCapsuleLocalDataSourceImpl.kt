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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
        startDate: LocalDate,
        endDate: LocalDate,
    ): PagingSource<Int, TimeCapsuleEntity> {
        return timeCapsuleDao.pagingSource(
            status = status,
            startDate = startDate.atStartOfDay(),
            endDate = endDate.atTime(LocalTime.MAX),
        ).mapValue {
            TimeCapsuleMapper.toData(it)
        }
    }

    override suspend fun clearByCondition(status: String, startDate: LocalDate, endDate: LocalDate): Boolean {
        try {
            timeCapsuleDao.clearByCondition(
                status = status,
                startDate = startDate.atStartOfDay(),
                endDate = endDate.atTime(LocalTime.MAX),
            )
            return true
        } catch (e: Exception) {
            Logger.e("clearByCondition error: $e")
            return false
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
