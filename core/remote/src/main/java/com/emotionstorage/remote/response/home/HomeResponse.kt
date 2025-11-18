package com.emotionstorage.remote.response.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    // 보유 티켓 수
    val remainingTickets: Int,
    // 하루 최대 보유 가능한 티켓 수
    val dailyLimit: Int,
    // 보유 열쇠 개수
    val keyCount: Int,
    val hasNewNotification: Boolean,
    val hasNewTimeCapsule: Boolean,
    // 가장 최근 날짜의 일일 리포트 열람 여부
    val hasNewReport: Boolean,
    // 가장 최근 날짜의 일일 리포트 ID
    val reportId: Long? = null,
)
