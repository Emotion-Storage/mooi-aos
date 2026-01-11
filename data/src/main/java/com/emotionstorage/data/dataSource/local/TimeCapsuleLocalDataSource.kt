package com.emotionstorage.data.dataSource.local

import androidx.paging.PagingSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TimeCapsuleLocalDataSource {
    fun getPagingSource(
        status: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): PagingSource<Int, TimeCapsuleEntity>

    suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean

    suspend fun clearAll(): Boolean
}
