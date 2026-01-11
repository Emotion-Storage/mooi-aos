package com.emotionstorage.data.dataSource.local

import com.emotionstorage.data.model.FavoriteTimeCapsuleRemoteKeyEntity

interface FavoriteTimeCapsuleRemoteKeyLocalDataSource {
    suspend fun remoteKeyById(
        id: Long,
        queryKey: String,
    ): FavoriteTimeCapsuleRemoteKeyEntity?

    suspend fun insertAll(keys: List<FavoriteTimeCapsuleRemoteKeyEntity>)

    suspend fun clearByQueryKey(queryKey: String)

    suspend fun lastUpdated(queryKey: String): Long?
}
