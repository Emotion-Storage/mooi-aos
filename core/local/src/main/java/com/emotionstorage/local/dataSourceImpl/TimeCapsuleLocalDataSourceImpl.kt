package com.emotionstorage.local.dataSourceImpl

import androidx.paging.PagingSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.local.modelMapper.TimeCapsuleMapper
import com.emotionstorage.local.room.dao.TimeCapsuleDao
import com.emotionstorage.local.util.mapValue
import com.orhanobut.logger.Logger
import javax.inject.Inject

class TimeCapsuleLocalDataSourceImpl @Inject constructor(
    private val timeCapsuleDao: TimeCapsuleDao,
) : TimeCapsuleLocalDataSource {
    private var lastUpdated: Long = System.currentTimeMillis()

    override suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean {
        try {
            timeCapsuleDao.upsertAll(timeCapsules.map { TimeCapsuleMapper.toLocal(it) })
            lastUpdated = System.currentTimeMillis()
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

    override suspend fun lastUpdated(): Long = lastUpdated

    override suspend fun clearAll(): Boolean {
        try {
            timeCapsuleDao.clearAll()
            lastUpdated = System.currentTimeMillis()
            return true
        } catch (e: Exception) {
            Logger.e("clearAll error: $e")
            return false
        }
    }
}
