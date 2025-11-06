package com.emotionstorage.remote.request.fcm

import kotlinx.serialization.Serializable

@Serializable
data class PostFcmTokenRequest(
    val fcmToken: String,
)
