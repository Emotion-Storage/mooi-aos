package com.emotionstorage.remote.response

import com.emotionstorage.common.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ResponseDto<T>(
    val status: Int,
    val code: String,
    val message: String?,
    val data: T?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime
)