package com.emotionstorage.home.data.modelMapper

import com.emotionstorage.home.data.model.HomeEntity
import com.emotionstorage.home.domain.model.Home

object HomeMapper {
    fun toDomain(entity: HomeEntity): Home =
        Home(
            ticketCount = entity.ticketCount,
            keyCount = entity.keyCount,
            hasNewNotification = entity.hasNewNotification,
            hasNewTimeCapsule = entity.hasNewTimeCapsule,
            hasNewReport = entity.hasNewReport,
        )
}
