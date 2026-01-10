package com.emotionstorage.data.dataSource.local

import androidx.paging.PagingSource
import com.emotionstorage.data.model.TimeCapsuleEntity

interface TimeCapsuleLocalDataSource {
    fun getPagingSource(): PagingSource<Int, TimeCapsuleEntity>

    suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean

    suspend fun lastUpdated(): Long

    suspend fun clearAll(): Boolean
}
