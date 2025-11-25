package com.emotionstorage.remote.response.timeCapsule

import kotlinx.serialization.Serializable

@Serializable
data class CreateTimeCapsuleResponse(
    val timeCapsuleId: Long,
)
