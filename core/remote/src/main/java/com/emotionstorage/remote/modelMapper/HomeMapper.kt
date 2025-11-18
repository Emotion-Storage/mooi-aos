package com.emotionstorage.remote.modelMapper

import com.emotionstorage.data.model.HomeEntity
import com.emotionstorage.remote.response.home.HomeResponse

object HomeMapper {
    fun toData(remote: HomeResponse): HomeEntity =
        HomeEntity(
            ticketCount = remote.remainingTickets,
            ticketLimit = remote.dailyLimit,
            keyCount = remote.keyCount,
            hasNewNotification = remote.hasNewNotification,
            hasNewTimeCapsule = remote.hasNewTimeCapsule,
            hasNewReport = remote.hasNewReport,
            newReportId = remote.reportId
        )
}
