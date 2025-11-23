package com.emotionstorage.remote.response.timeCapsule

import com.emotionstorage.data.model.TimeCapsuleEntity
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CreateTimeCapsuleResponse(
    val timeCapsuleId: Long,
)

