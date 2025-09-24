package com.emotionstorage.home.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponseData(
    val remainingTickets: Int, // 보유 티켓 수
    val dailyLimit: Int, // 하루 최대 보유 가능한 티켓 수
    val keyCount: Int,
    val hasNewNotification: Boolean,
    val notificationCount: Int,
    val hasNewTimeCapsule: Boolean,
    val timeCapsuleCount: Int,
    val hasNewReport: Boolean,
    val reportCount: Int
)
