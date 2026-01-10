package com.emotionstorage.data.dataSource.local

import androidx.paging.PagingSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import kotlinx.coroutines.flow.Flow

interface TimeCapsuleLocalDataSource {
    fun getPagingSource(): PagingSource<Int, TimeCapsuleEntity>

    suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean

    suspend fun lastUpdated(): Flow<Long?>

    suspend fun clearAll(): Boolean
}
