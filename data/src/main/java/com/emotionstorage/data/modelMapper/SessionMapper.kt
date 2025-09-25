package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.SessionEntity
import com.emotionstorage.domain.model.Session

internal object SessionMapper {
    fun toDomain(entity: SessionEntity): Session =
        Session(
            accessToken = entity.accessToken,
        )

    fun toData(domain: Session): SessionEntity =
        SessionEntity(
            accessToken = domain.accessToken,
        )
}
