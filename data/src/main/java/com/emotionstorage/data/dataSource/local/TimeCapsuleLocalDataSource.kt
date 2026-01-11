package com.emotionstorage.data.dataSource.local

import androidx.paging.PagingSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface TimeCapsuleLocalDataSource {
    fun getPagingSource(
        status: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): PagingSource<Int, TimeCapsuleEntity>

    suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean

    suspend fun clearByCondition(
        status: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean

    suspend fun clearAll(): Boolean
}
