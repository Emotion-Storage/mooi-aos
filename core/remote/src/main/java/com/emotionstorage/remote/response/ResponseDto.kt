package com.emotionstorage.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto<T>(
    val status: ResponseStatus,
    val message: String?,
    val data: T?
)