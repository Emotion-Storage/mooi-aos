package com.emotionstorage.local.dataSourceImpl

import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.local.modelMapper.TimeCapsuleMapper
import com.emotionstorage.local.room.dao.TimeCapsuleDao
import com.emotionstorage.local.util.mapValue
import com.orhanobut.logger.Logger
import javax.inject.Inject

class TimeCapsuleLocalDataSourceImpl @Inject constructor(
    private val timeCapsuleDao: TimeCapsuleDao
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

    override val pagingSource = timeCapsuleDao.pagingSource().mapValue {
        TimeCapsuleMapper.toData(it)
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
