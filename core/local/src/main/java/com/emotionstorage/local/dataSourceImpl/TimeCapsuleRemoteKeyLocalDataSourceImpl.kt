package com.emotionstorage.local.dataSourceImpl

import com.emotionstorage.data.dataSource.local.TimeCapsuleRemoteKeyLocalDataSource
import com.emotionstorage.data.model.TimeCapsuleRemoteKeyEntity
import com.emotionstorage.local.modelMapper.TimeCapsuleRemoteKeyMapper
import com.emotionstorage.local.room.dao.TimeCapsuleRemoteKeyDao
import javax.inject.Inject

class TimeCapsuleRemoteKeyLocalDataSourceImpl @Inject constructor(
    private val dao: TimeCapsuleRemoteKeyDao,
) : TimeCapsuleRemoteKeyLocalDataSource {
    override suspend fun remoteKeyById(
        id: Long,
        queryKey: String
    ): TimeCapsuleRemoteKeyEntity? =
        dao.remoteKeyById(id, queryKey)?.let {
            TimeCapsuleRemoteKeyMapper.toEntity(
                it
            )
        }


    override suspend fun insertAll(keys: List<TimeCapsuleRemoteKeyEntity>) {
        dao.insertAll(
            keys.map {
                TimeCapsuleRemoteKeyMapper.toLocal(it)
            }
        )
    }

    override suspend fun clearByQueryKey(queryKey: String) {
        dao.clearByQueryKey(queryKey)
    }

    override suspend fun lastUpdated(queryKey: String): Long? =
        dao.lastUpdated(queryKey)
}
