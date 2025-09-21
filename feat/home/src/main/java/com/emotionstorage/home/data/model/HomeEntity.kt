package com.emotionstorage.home.data.model

data class HomeEntity (
    val ticketCount: Int,
    val keyCount: Int,
    val hasNewNotification: Boolean,
    val hasNewTimeCapsule: Boolean,
    val hasNewReport: Boolean,
)