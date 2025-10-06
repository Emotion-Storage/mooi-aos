package com.emotionstorage.data.dataSource

import com.emotionstorage.data.model.TimeCapsuleEntity

interface TimeCapsuleRemoteDataSource {
    suspend fun patchTimeCapsuleOpen(id: String): Boolean

    suspend fun patchTimeCapsuleNote(
        id: String,
        note: String,
    ): Boolean

    suspend fun getFavoriteTimeCapsules(sortBy: String): List<TimeCapsuleEntity>
}
