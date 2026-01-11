package com.emotionstorage.data.dataSource.local

import com.emotionstorage.data.model.TimeCapsuleEntity
import java.time.LocalDate

interface TimeCapsuleLocalDataSource {
    suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean

    suspend fun clearByCondition(
        status: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean

    suspend fun clearFavorites(): Boolean

    suspend fun clearAll(): Boolean
}
