package com.emotionstorage.home.domain.model

data class Home(
    val ticketCount: Int,
    val keyCount: Int,
    val hasNewNotification: Boolean,
    val hasNewTimeCapsule: Boolean,
    val hasNewReport: Boolean,
)
