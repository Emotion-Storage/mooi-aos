package com.emotionstorage.data.dataSource.local

import com.emotionstorage.data.model.TimeCapsuleEntity

interface TimeCapsuleLocalDataSource {
    suspend fun saveTimeCapsules(timeCapsules: List<TimeCapsuleEntity>): Boolean
    suspend fun clearAll(): Boolean
}
