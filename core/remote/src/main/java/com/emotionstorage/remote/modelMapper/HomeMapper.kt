package com.emotionstorage.remote.modelMapper

import com.emotionstorage.remote.response.home.HomeResponse

object HomeMapper {
    fun toData(remote: HomeResponse): HomeEntity =
        HomeEntity(
            ticketCount = remote.remainingTickets,
            keyCount = remote.keyCount,
            hasNewNotification = remote.hasNewNotification,
            hasNewTimeCapsule = remote.hasNewTimeCapsule,
            hasNewReport = remote.hasNewReport,
        )
}
