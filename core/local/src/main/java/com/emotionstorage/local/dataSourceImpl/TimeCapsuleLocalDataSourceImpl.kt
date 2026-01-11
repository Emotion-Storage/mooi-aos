package com.emotionstorage.local.dataSourceImpl

import androidx.paging.PagingSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.data.model.TimeCapsuleLocal
import com.emotionstorage.data.modelMapper.TimeCapsuleLocalMapper
import com.emotionstorage.local.room.dao.TimeCapsuleDao
import com.orhanobut.logger.Logger
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class TimeCapsuleLocalDataSourceImpl @Inject constructor(
    private val timeCapsuleDao: TimeCapsuleDao,
) : TimeCapsuleLocalDataSource {
    override suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean {
        try {
            timeCapsuleDao.upsertAll(timeCapsules.map { TimeCapsuleLocalMapper.toLocal(it) })
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
    ): PagingSource<Int, TimeCapsuleLocal> {
        return timeCapsuleDao
            .pagingSource(
                status = status,
                startDate = startDate.atStartOfDay(),
                endDate = endDate.atTime(LocalTime.MAX),
            )
    }

    override fun getFavoritePagingSource(sortBy: String): PagingSource<Int, TimeCapsuleLocal> {
        return timeCapsuleDao
            .favoritePagingSource(
                sortBy = sortBy,
            )
    }


    override suspend fun clearByCondition(
        status: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean {
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

    override suspend fun clearFavorites(): Boolean {
        try {
            timeCapsuleDao.clearFavorites()
            return true
        } catch (e: Exception) {
            Logger.e("clearFavorites error: $e")
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
