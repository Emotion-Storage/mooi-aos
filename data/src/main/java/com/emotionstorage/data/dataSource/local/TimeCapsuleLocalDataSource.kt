package com.emotionstorage.data.dataSource.local

import androidx.paging.PagingSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import java.time.LocalDate

interface TimeCapsuleLocalDataSource {
    fun getPagingSource(
        status: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): PagingSource<Int, TimeCapsuleEntity>

    fun getFavoritePagingSource(sortBy: String): PagingSource<Int, TimeCapsuleEntity>

    suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean

    suspend fun clearByCondition(
        status: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean

    suspend fun clearFavorites(): Boolean

    suspend fun clearAll(): Boolean
}
