package com.emotionstorage.home.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponseData(
    // 보유 티켓 수
    val remainingTickets: Int,
    // 하루 최대 보유 가능한 티켓 수
    val dailyLimit: Int,
    val keyCount: Int,
    val hasNewNotification: Boolean,
    val notificationCount: Int,
    val hasNewTimeCapsule: Boolean,
    val timeCapsuleCount: Int,
    val hasNewReport: Boolean,
    val reportCount: Int,
)
