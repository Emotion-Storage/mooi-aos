package com.emotionstorage.local.modelMapper

import com.emotionstorage.data.model.FavoriteTimeCapsuleRemoteKeyEntity
import com.emotionstorage.local.model.FavoriteTimeCapsuleRemoteKeyLocal

internal object FavoriteTimeCapsuleRemoteKeyMapper {
    fun toEntity(local: FavoriteTimeCapsuleRemoteKeyLocal) =
        FavoriteTimeCapsuleRemoteKeyEntity(
            id = local.id,
            queryKey = local.queryKey,
            prevPage = local.prevPage,
            nextPage = local.nextPage,
            lastUpdated = local.lastUpdated,
        )

    fun toLocal(entity: FavoriteTimeCapsuleRemoteKeyEntity) =
        FavoriteTimeCapsuleRemoteKeyLocal(
            id = entity.id,
            queryKey = entity.queryKey,
            prevPage = entity.prevPage,
            nextPage = entity.nextPage,
            lastUpdated = entity.lastUpdated,
        )
}
