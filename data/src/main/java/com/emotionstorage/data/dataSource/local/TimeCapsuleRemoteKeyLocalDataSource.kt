package com.emotionstorage.data.dataSource.local

import com.emotionstorage.data.model.TimeCapsuleRemoteKeyEntity

interface TimeCapsuleRemoteKeyLocalDataSource {
    suspend fun remoteKeyById(
        id: Long,
        queryKey: String,
    ): TimeCapsuleRemoteKeyEntity?

    suspend fun insertAll(keys: List<TimeCapsuleRemoteKeyEntity>)

    suspend fun clearByQueryKey(queryKey: String)

    suspend fun lastUpdated(queryKey: String): Long?
}
