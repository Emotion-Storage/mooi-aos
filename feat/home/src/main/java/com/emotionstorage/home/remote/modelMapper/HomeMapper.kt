package com.emotionstorage.home.remote.modelMapper

import com.emotionstorage.home.data.model.HomeEntity
import com.emotionstorage.home.remote.response.HomeResponseData

object HomeMapper {
    fun toData(remote: HomeResponseData): HomeEntity = HomeEntity(
        ticketCount = remote.remainingTickets,
        keyCount = remote.keyCount,
        hasNewNotification = remote.hasNewNotification,
        hasNewTimeCapsule = remote.hasNewTimeCapsule,
        hasNewReport = remote.hasNewReport
    )
}
