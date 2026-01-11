package com.emotionstorage.local.modelMapper

import com.emotionstorage.data.model.TimeCapsuleRemoteKeyEntity
import com.emotionstorage.local.model.TimeCapsuleRemoteKeyLocal

internal object TimeCapsuleRemoteKeyMapper {
    fun toEntity(local: TimeCapsuleRemoteKeyLocal) = TimeCapsuleRemoteKeyEntity(
        id = local.id,
        queryKey = local.queryKey,
        nextPage = local.nextPage,
        prevPage = local.prevPage,
        lastUpdated = local.lastUpdated,
    )

    fun toLocal(entity: TimeCapsuleRemoteKeyEntity) = TimeCapsuleRemoteKeyLocal(
        id = entity.id,
        queryKey = entity.queryKey,
        nextPage = entity.nextPage,
        prevPage = entity.prevPage,
        lastUpdated = entity.lastUpdated,
    )
}
