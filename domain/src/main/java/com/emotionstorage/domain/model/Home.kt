package com.emotionstorage.domain.model

data class Home(
    val ticketCount: Int,
    val ticketLimit: Int,
    val keyCount: Int,
    val hasNewNotification: Boolean,
    val hasNewTimeCapsule: Boolean,
    val hasNewReport: Boolean,
    val newReportId: Long? = null
)
