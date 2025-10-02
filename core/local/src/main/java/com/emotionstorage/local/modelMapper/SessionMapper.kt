package com.emotionstorage.local.modelMapper

import com.emotionstorage.data.model.SessionEntity
import com.emotionstorage.local.model.SessionLocal

internal object SessionMapper {
    fun toLocal(entity: SessionEntity) =
        SessionLocal(
            accessToken = entity.accessToken,
        )

    fun toEntity(local: SessionLocal) =
        SessionEntity(
            accessToken = local.accessToken,
        )
}
