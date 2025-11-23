package com.emotionstorage.remote.response

import java.time.LocalDateTime

class CustomHttpException(
    val status: ResponseStatus,
    val code: String? = null,
    val data: Any? = null,
    val timestamp: LocalDateTime,
    override val message: String? = null,
    override val cause: Throwable? = null
) : Throwable()
