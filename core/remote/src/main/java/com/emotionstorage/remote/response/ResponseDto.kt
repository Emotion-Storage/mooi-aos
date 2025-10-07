package com.emotionstorage.remote.response

import com.emotionstorage.common.LocalDateTimeSerializer
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.MyPage
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

inline fun <T, R> ResponseDto<T>.toDataState(
    crossinline map: (T) -> R
): DataState<R> {
    return if (status in 200..299) {
        val body = data
            ?: return DataState.Error(IllegalStateException("Empty body"))
        DataState.Success(map(body))
    } else if (status in 400..499) {
        DataState.Error(Exception(message ?: "Client Error"))
    } else if (status in 500..599) {
        DataState.Error(Exception(message ?: "Server Error"))
    } else {
        DataState.Error(Exception(message ?: "Unknown Error"))

    }
}
