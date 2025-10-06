package com.emotionstorage.remote.response

import com.emotionstorage.common.LocalDateTimeSerializer
import com.emotionstorage.domain.common.DataState
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ResponseDto<T>(
    val status: Int,
    val code: String? = null,
    val message: String? = null,
    val data: T? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime? = null,
)

fun <T> ResponseDto<T>.toEmptyDataState(onSuccessMap: (T?) -> Unit = {}): DataState<Unit> =
    if (status in 200..299) {
        DataState.Success(Unit)
    } else {
        DataState.Error(Exception(message ?: "Unknown Error"))
    }
