package com.emotionstorage.remote.request.timeCapsule

import kotlinx.serialization.Serializable

@Serializable
data class PostTimeCapsuleOpenAtRequest(
    val capsuleId: Long,
    val storedAt: String,
    val openAt: String,
)
