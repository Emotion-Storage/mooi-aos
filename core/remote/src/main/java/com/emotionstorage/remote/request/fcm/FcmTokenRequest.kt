package com.emotionstorage.remote.request.fcm

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest(
    val fcmToken: String,
)
