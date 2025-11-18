package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.HomeEntity
import com.emotionstorage.domain.model.Home

object HomeMapper {
    fun toDomain(entity: HomeEntity): Home =
        Home(
            ticketCount = entity.ticketCount,
            ticketLimit = entity.ticketLimit,
            keyCount = entity.keyCount,
            hasNewNotification = entity.hasNewNotification,
            hasNewTimeCapsule = entity.hasNewTimeCapsule,
            hasNewReport = entity.hasNewReport,
            newReportId = entity.newReportId
        )
}
