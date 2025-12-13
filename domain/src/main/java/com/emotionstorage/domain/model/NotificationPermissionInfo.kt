package com.emotionstorage.domain.model

enum class NotificationPermissionStatus {
    Unknown,
    Granted,
    Denied,
    DeniedAlways,
}

data class NotificationPermissionInfo(
    val status: NotificationPermissionStatus,
    val hasPrompted: Boolean,
)
