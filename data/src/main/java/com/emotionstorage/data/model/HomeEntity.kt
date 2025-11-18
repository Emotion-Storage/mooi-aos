package com.emotionstorage.data.model

data class HomeEntity(
    val ticketCount: Int,
    val ticketLimit: Int,
    val keyCount: Int,
    val hasNewNotification: Boolean,
    val hasNewTimeCapsule: Boolean,
    val hasNewReport: Boolean,
    val newReportId: Long? = null,
)
