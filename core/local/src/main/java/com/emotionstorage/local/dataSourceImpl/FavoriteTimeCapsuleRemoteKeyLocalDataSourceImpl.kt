package com.emotionstorage.local.dataSourceImpl

import com.emotionstorage.data.dataSource.local.FavoriteTimeCapsuleRemoteKeyLocalDataSource
import com.emotionstorage.data.model.FavoriteTimeCapsuleRemoteKeyEntity
import com.emotionstorage.local.modelMapper.FavoriteTimeCapsuleRemoteKeyMapper
import com.emotionstorage.local.room.dao.FavoriteTimeCapsuleRemoteKeyDao
import javax.inject.Inject

class FavoriteTimeCapsuleRemoteKeyLocalDataSourceImpl @Inject constructor(
    private val dao: FavoriteTimeCapsuleRemoteKeyDao,
) : FavoriteTimeCapsuleRemoteKeyLocalDataSource {
    override suspend fun remoteKeyById(
        id: Long,
        queryKey: String,
    ): FavoriteTimeCapsuleRemoteKeyEntity? =
        dao.remoteKeyById(id, queryKey)?.let { FavoriteTimeCapsuleRemoteKeyMapper.toEntity(it) }

    override suspend fun insertAll(keys: List<FavoriteTimeCapsuleRemoteKeyEntity>) =
        dao.insertAll(keys.map { FavoriteTimeCapsuleRemoteKeyMapper.toLocal(it) })

    override suspend fun clearByQueryKey(queryKey: String) = dao.clearByQueryKey(queryKey)

    override suspend fun lastUpdated(queryKey: String): Long? = dao.lastUpdated(queryKey)
}
